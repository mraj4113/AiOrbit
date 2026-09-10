(() => {
  'use strict';

  const API = '/api/companies';
  const params = new URLSearchParams(window.location.search);

  const state = {
    category: params.get('category') || '',
    search: params.get('search') || '',
    sort: params.get('sort') || 'rating',
    direction: params.get('direction') || 'desc',
    page: Math.max(parseInt(params.get('page') || '0', 10) || 0, 0),
    size: 9,
    view: localStorage.getItem('aio-view') || 'grid'
  };

  const $ = (id) => document.getElementById(id);
  const els = {
    cards: $('cards'),
    count: $('result-count'),
    input: $('search-input'),
    searchBtn: $('search-btn'),
    suggestions: $('suggestions'),
    sort: $('sort-select'),
    pagination: $('pagination'),
    empty: $('empty-state'),
    clear: $('clear-filters'),
    chips: [...document.querySelectorAll('.chip')],
    viewBtns: [...document.querySelectorAll('.view-btn')]
  };

  const esc = (s) => String(s ?? '').replace(/[&<>"']/g, (m) =>
    ({ '&': '&amp;', '<': '&lt;', '>': '&gt;', '"': '&quot;', "'": '&#39;' }[m]));

  // ---------- icons ----------
  const starIcon = '<svg class="h-4 w-4 text-white" fill="currentColor" viewBox="0 0 24 24"><path d="M12 2l3.09 6.26L22 9.27l-5 4.87 1.18 6.88L12 17.77l-6.18 3.25L7 14.14 2 9.27l6.91-1.01z"/></svg>';
  const checkIcon = '<svg class="h-4 w-4 text-white" fill="currentColor" viewBox="0 0 24 24"><path d="M9 16.17 4.83 12l-1.42 1.41L9 19 21 7l-1.41-1.41z"/></svg>';
  const arrowIcon = '<svg class="h-4 w-4 text-neutral-600 transition-all duration-300 group-hover:translate-x-1 group-hover:text-white" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24"><path d="M5 12h14m-6-6 6 6-6 6"/></svg>';

  // ---------- rendering ----------
  const cardHTML = (c) => `
    <a href__="/companies/${esc(c.slug)}" class="card-glow group block rounded-2xl bg-gradient-to-br from-white/[0.14] via-white/[0.05] to-transparent p-[1px] transition-transform duration-300 hover:-translate-y-1">
      <div class="h-full rounded-2xl bg-[#171717] p-6">
        <div class="flex items-start justify-between">
          <img src="${esc(c.logoUrl || '')}" alt="${esc(c.name)}" width="48" height="48"
               class="h-12 w-12 rounded-xl border border-[#262626] bg-white/[0.04] p-1.5 object-contain"
               onerror="this.style.visibility='hidden'"/>
          ${c.featured ? '<span class="rounded-full border border-[#262626] bg-white/[0.06] px-2.5 py-1 text-[10px] font-bold uppercase tracking-wider text-white">Featured</span>' : ''}
        </div>
        <h3 class="mt-4 flex items-center gap-1.5 text-lg font-bold tracking-tight">
          <span>${esc(c.name)}</span>${c.verified ? checkIcon : ''}
        </h3>
        <p class="mt-1 text-[13px] text-[#a3a3a3]">${esc(c.category)}</p>
        <div class="mt-5 flex items-center justify-between border-t border-[#262626] pt-4">
          <div class="flex items-center gap-1.5">
            ${starIcon}<span class="text-sm font-bold">${Number(c.rating).toFixed(1)}</span>
            <span class="text-xs text-[#a3a3a3]">${c.reviewCount} reviews</span>
          </div>
          ${arrowIcon}
        </div>
      </div>
    </a>`;

  function applyView() {
    els.cards.className = state.view === 'list'
      ? 'flex flex-col gap-4'
      : 'grid grid-cols-1 gap-5 sm:grid-cols-2 lg:grid-cols-3';
    els.viewBtns.forEach((btn) => {
      const active = btn.dataset.view === state.view;
      btn.classList.toggle('bg-white', active);
      btn.classList.toggle('text-black', active);
      btn.classList.toggle('text-neutral-400', !active);
    });
  }

  function syncChips() {
    els.chips.forEach((chip) => {
      const active = (chip.dataset.category || '') === state.category;
      chip.classList.toggle('bg-white', active);
      chip.classList.toggle('text-black', active);
      chip.classList.toggle('border-white', active);
    });
  }

  const paginationHTML = (r) => {
    if (r.totalPages <= 1) return '';
    const btn = (label, page, enabled) => enabled
      ? `<button class="page-btn rounded-full border border-[#262626] px-4 py-2 text-sm text-neutral-300 transition-all hover:border-white/40 hover:text-white" data-page="${page}">${label}</button>`
      : '';
    return `${btn('Previous', r.page - 1, r.hasPrevious)}
      <span class="rounded-full border border-white bg-white px-4 py-2 text-sm font-semibold text-black">${r.page + 1} / ${r.totalPages}</span>
      ${btn('Next', r.page + 1, r.hasNext)}`;
  };

  function render(r) {
    els.cards.innerHTML = r.items.map(cardHTML).join('');
    els.count.textContent = r.totalElements;
    els.pagination.innerHTML = paginationHTML(r);
    els.empty.style.display = r.items.length ? 'none' : 'flex';
    els.pagination.querySelectorAll('.page-btn').forEach((b) =>
      b.addEventListener('click', () => { state.page = +b.dataset.page; load(); }));
    syncChips();
    applyView();
    bindGlow();
  }

  function bindGlow() {
    document.querySelectorAll('.card-glow').forEach((card) => {
      card.addEventListener('mousemove', (e) => {
        const rect = card.getBoundingClientRect();
        card.style.setProperty('--mx', `${e.clientX - rect.left}px`);
        card.style.setProperty('--my', `${e.clientY - rect.top}px`);
      });
    });
  }

  // ---------- data ----------
  function query() {
    const q = new URLSearchParams({
      sort: state.sort,
      direction: state.direction,
      page: state.page,
      size: state.size
    });
    if (state.category) q.set('category', state.category);
    if (state.search) q.set('search', state.search);
    return q;
  }

  async function load() {
    els.cards.classList.add('opacity-50', 'pointer-events-none');
    try {
      const res = await fetch(`${API}?${query()}`);
      if (!res.ok) throw new Error('Request failed');
      render(await res.json());
      const qs = query().toString();
      history.pushState({}, '', qs ? `/companies?${qs}` : '/companies');
    } finally {
      els.cards.classList.remove('opacity-50', 'pointer-events-none');
    }
  }

  // ---------- suggestions (typing never reloads; results only on Enter / button) ----------
  const hideSuggestions = () => els.suggestions.classList.add('hidden');
  let suggestTimer;

  function showSuggestions(items) {
    if (!items.length) { hideSuggestions(); return; }
    els.suggestions.innerHTML = items.map((c) => `
      <button class="suggestion flex w-full items-center justify-between gap-3 px-4 py-3 text-left transition-colors hover:bg-white/[0.06]"
              data-slug="${esc(c.slug)}">
        <span class="flex items-center gap-3">
          <img src="${esc(c.logoUrl || '')}" alt="" class="h-8 w-8 rounded-lg border border-[#262626] bg-white/[0.04] p-1 object-contain"
               onerror="this.style.visibility='hidden'"/>
          <span>
            <span class="block text-sm font-semibold text-white">${esc(c.name)}</span>
            <span class="block text-xs text-[#a3a3a3]">${esc(c.category)}</span>
          </span>
        </span>
        ${arrowIcon}
      </button>`).join('');
    els.suggestions.classList.remove('hidden');
    els.suggestions.querySelectorAll('.suggestion').forEach((btn) =>
      btn.addEventListener('click', () => {
        window.location.href = '/companies/' + btn.dataset.slug;
      }));
  }

  els.input.addEventListener('input', () => {
    clearTimeout(suggestTimer);
    const q = els.input.value.trim();
    if (!q) { hideSuggestions(); return; }
    suggestTimer = setTimeout(async () => {
      const res = await fetch(`${API}/suggest?q=${encodeURIComponent(q)}`);
      showSuggestions(await res.json());
    }, 250);
  });

  function runSearch() {
    hideSuggestions();
    state.search = els.input.value.trim();
    state.page = 0;
    load();
  }

  els.input.addEventListener('keydown', (e) => {
    if (e.key === 'Enter') { e.preventDefault(); runSearch(); }
    if (e.key === 'Escape') hideSuggestions();
  });

  els.searchBtn.addEventListener('click', runSearch);

  document.addEventListener('click', (e) => {
    if (e.target !== els.input && !els.suggestions.contains(e.target)) hideSuggestions();
  });

  // ---------- filters / sort / view / pagination / clear ----------
  els.chips.forEach((chip) => chip.addEventListener('click', () => {
    state.category = chip.dataset.category || '';
    state.page = 0;
    load();
  }));

  els.sort.value = state.sort;
  els.sort.addEventListener('change', () => {
    state.sort = els.sort.value;
    state.direction = state.sort === 'name' ? 'asc' : 'desc';
    state.page = 0;
    load();
  });

  els.viewBtns.forEach((btn) => btn.addEventListener('click', () => {
    state.view = btn.dataset.view;
    localStorage.setItem('aio-view', state.view);
    applyView();
  }));

  els.clear.addEventListener('click', () => {
    Object.assign(state, { category: '', search: '', sort: 'rating', direction: 'desc', page: 0 });
    els.input.value = '';
    els.sort.value = 'rating';
    load();
  });

  // ---------- browser back/forward ----------
  window.addEventListener('popstate', () => {
    const p = new URLSearchParams(window.location.search);
    Object.assign(state, {
      category: p.get('category') || '',
      search: p.get('search') || '',
      sort: p.get('sort') || 'rating',
      direction: p.get('direction') || 'desc',
      page: Math.max(parseInt(p.get('page') || '0', 10) || 0, 0)
    });
    els.input.value = state.search;
    els.sort.value = state.sort;
    load();
  });

  // ---------- init ----------
  els.input.value = state.search;
  syncChips();
  applyView();
  bindGlow();
})();