<template>
  <div class="dash-root">

    <!-- ═══ SIDEBAR ═══ -->
    <aside class="sidebar" :class="{ 'sidebar-open': sidebarOpen }">

      <!-- Brand -->
      <div class="sb-brand">
        <div class="sb-logo">
          <svg width="18" height="18" fill="none" stroke="white" stroke-width="2" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" d="M4.318 6.318a4.5 4.5 0 000 6.364L12 20.364l7.682-7.682a4.5 4.5 0 00-6.364-6.364L12 7.636l-1.318-1.318a4.5 4.5 0 00-6.364 0z"/>
          </svg>
        </div>
        <div>
          <p class="sb-appname">AppSanté</p>
          <p class="sb-tagline">Espace Patient</p>
        </div>
      </div>

      <!-- Nav -->
      <p class="sb-section-lbl">NAVIGATION</p>
      <nav class="sb-nav">
        <button v-for="item in navItems" :key="item.key"
          :class="['sb-item', { active: activeNav === item.key }]"
          @click="activeNav = item.key; sidebarOpen = false; if(item.key === 'rdv') resetRdv()">
          <span class="sb-item-icon" v-html="item.icon"></span>
          <span class="sb-item-lbl">{{ item.label }}</span>
          <span v-if="item.key === 'mesrdv'" class="sb-count">{{ allRdv.length }}</span>
        </button>
      </nav>

      <div style="flex:1"></div>

      <!-- Support links -->
      <p class="sb-section-lbl">COMPTE</p>
      <div class="sb-nav" style="margin-bottom:12px">
        <button class="sb-item" @click="logout">
          <span class="sb-item-icon">
            <svg width="16" height="16" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" d="M17 16l4-4m0 0l-4-4m4 4H7m6 4v1a3 3 0 01-3 3H6a3 3 0 01-3-3V7a3 3 0 013-3h4a3 3 0 013 3v1"/></svg>
          </span>
          <span class="sb-item-lbl">Déconnexion</span>
        </button>
      </div>

      <!-- User card -->
      <div class="sb-usercard">
        <div class="sb-useravatar">{{ userInitials }}</div>
        <div class="sb-userinfo">
          <p class="sb-username">{{ userName }}</p>
          <div class="sb-online">
            <span class="sb-online-dot"></span>
            <span>En ligne</span>
          </div>
        </div>
      </div>

    </aside>

    <div v-if="sidebarOpen" class="sidebar-overlay" @click="sidebarOpen = false"></div>

    <!-- ═══ MAIN ═══ -->
    <main class="main-content">

      <!-- Top bar -->
      <header class="topbar">
        <button class="tb-hamburger" @click="sidebarOpen = true">
          <svg width="20" height="20" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" d="M4 6h16M4 12h16M4 18h16"/></svg>
        </button>
        <div class="tb-breadcrumb">
          <span class="tb-app">AppSanté</span>
          <svg width="12" height="12" fill="none" stroke="#94a3b8" stroke-width="2.5" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" d="M9 5l7 7-7 7"/></svg>
          <span class="tb-page">{{ navItems.find(n => n.key === activeNav)?.label }}</span>
        </div>
        <div class="tb-right">
          <span class="tb-date-chip">{{ todayStr }}</span>
          <div class="tb-notif">
            <svg width="19" height="19" fill="none" stroke="#64748b" stroke-width="2" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" d="M15 17h5l-1.405-1.405A2.032 2.032 0 0118 14.158V11a6.002 6.002 0 00-4-5.659V5a2 2 0 10-4 0v.341C7.67 6.165 6 8.388 6 11v3.159c0 .538-.214 1.055-.595 1.436L4 17h5m6 0v1a3 3 0 11-6 0v-1m6 0H9"/></svg>
            <span class="tb-notif-badge">1</span>
          </div>
          <div class="user-avatar-top">{{ userInitials }}</div>
        </div>
      </header>

      <!-- ══ PAGE: Accueil ══ -->
      <div v-if="activeNav === 'home'" class="page-content">

        <!-- Hero banner -->
        <div class="hero-card">
          <div class="hero-left">
            <p class="hero-eyebrow">Bienvenue sur AppSanté</p>
            <h2 class="hero-title">Bonjour, {{ userName }} 👋</h2>
            <p class="hero-sub">Prenez rendez-vous avec les meilleurs médecins du Maroc en quelques clics.</p>
            <button class="hero-cta" @click="activeNav = 'rdv'; resetRdv()">
              <svg width="16" height="16" fill="none" stroke="currentColor" stroke-width="2.5" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z"/></svg>
              Prendre un rendez-vous
            </button>
          </div>
          <div class="hero-right">
            <div class="hero-float-card" v-if="nextRdv">
              <div class="hfc-top">
                <div class="hfc-avatar" :style="`background:${nextRdv.color}`">{{ nextRdv.initials }}</div>
                <div style="flex:1;min-width:0">
                  <p class="hfc-name">Dr. {{ nextRdv.doctor }}</p>
                  <p class="hfc-spec">{{ nextRdv.specialty }} · {{ nextRdv.lieu?.split(',')[1]?.trim() || '' }}</p>
                </div>
                <span class="hfc-badge">✓ Confirmé</span>
              </div>
              <div class="hfc-divider"></div>
              <div class="hfc-bottom">
                <span class="hfc-date">{{ nextRdv.date }}</span>
                <span class="hfc-time">{{ nextRdv.time }}</span>
              </div>
            </div>
            <div class="hero-float-card" v-else>
              <div class="hfc-top">
                <div class="hfc-avatar" style="background:#94a3b8">?</div>
                <div style="flex:1;min-width:0">
                  <p class="hfc-name">Aucun RDV prévu</p>
                  <p class="hfc-spec">Prenez votre premier rendez-vous</p>
                </div>
              </div>
              <div class="hfc-divider"></div>
              <div class="hfc-bottom">
                <span class="hfc-date">—</span>
                <span class="hfc-time">—</span>
              </div>
            </div>
          </div>
        </div>

        <!-- Stats -->
        <div class="stats-row">
          <div v-for="s in stats" :key="s.label" class="stat-tile">
            <div class="stat-tile-icon" :style="{ background: s.bg }"><span v-html="s.icon"></span></div>
            <div>
              <p class="stat-tile-val">{{ s.value }}</p>
              <p class="stat-tile-lbl">{{ s.label }}</p>
            </div>
          </div>
        </div>

        <!-- 2-col: prochain RDV + quick actions -->
        <div class="home-2col">

          <!-- Prochain RDV -->
          <div class="panel">
            <div class="panel-header">
              <h4>Prochain rendez-vous</h4>
              <span v-if="nextRdv" class="pill-green">● Confirmé</span>
            </div>
            <!-- RDV existant -->
            <div v-if="nextRdv" class="appt-card">
              <div class="appt-date-block">
                <p class="appt-day">{{ nextRdv.date?.split(' ')[0] }}</p>
                <p class="appt-month">{{ nextRdv.date?.split(' ')[1]?.toUpperCase() }}</p>
              </div>
              <div class="appt-vline"></div>
              <div class="appt-body">
                <div class="appt-doc-row">
                  <div class="appt-doc-avatar" :style="`background:${nextRdv.color}`">{{ nextRdv.initials }}</div>
                  <div>
                    <p class="appt-doc-name">Dr. {{ nextRdv.doctor }}</p>
                    <span class="appt-spec-pill">{{ nextRdv.specialty }}</span>
                  </div>
                </div>
                <p class="appt-meta-line">
                  <svg width="13" height="13" fill="none" stroke="#94a3b8" stroke-width="2" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z"/></svg>
                  {{ nextRdv.time }}
                </p>
                <p class="appt-meta-line">
                  <svg width="13" height="13" fill="none" stroke="#94a3b8" stroke-width="2" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" d="M17.657 16.657L13.414 20.9a1.998 1.998 0 01-2.827 0l-4.244-4.243a8 8 0 1111.314 0z"/><path stroke-linecap="round" stroke-linejoin="round" d="M15 11a3 3 0 11-6 0 3 3 0 016 0z"/></svg>
                  {{ nextRdv.lieu }}
                </p>
              </div>
            </div>
            <!-- Aucun RDV -->
            <div v-else class="appt-empty">
              <svg width="36" height="36" fill="none" stroke="#cbd5e1" stroke-width="1.5" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z"/></svg>
              <p>Aucun rendez-vous prévu</p>
              <button class="btn-submit-sm" @click="activeNav='rdv';resetRdv()">Prendre un RDV</button>
            </div>
          </div>

          <!-- Quick actions -->
          <div class="panel">
            <div class="panel-header"><h4>Actions rapides</h4></div>
            <div class="quick-list">
              <button class="quick-item" @click="activeNav = 'rdv'; resetRdv()">
                <div class="qi-icon" style="background:#eff6ff;color:#2563eb">
                  <svg width="20" height="20" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z"/></svg>
                </div>
                <div style="flex:1">
                  <p class="qi-title">Nouveau rendez-vous</p>
                  <p class="qi-sub">Choisissez votre spécialité et médecin</p>
                </div>
                <svg width="14" height="14" fill="none" stroke="#cbd5e1" stroke-width="2.5" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" d="M9 5l7 7-7 7"/></svg>
              </button>
              <button class="quick-item" @click="activeNav = 'dossier'">
                <div class="qi-icon" style="background:#f0fdf4;color:#16a34a">
                  <svg width="20" height="20" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z"/></svg>
                </div>
                <div style="flex:1">
                  <p class="qi-title">Mon dossier médical</p>
                  <p class="qi-sub">Consultations &amp; ordonnances</p>
                </div>
                <svg width="14" height="14" fill="none" stroke="#cbd5e1" stroke-width="2.5" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" d="M9 5l7 7-7 7"/></svg>
              </button>
              <button class="quick-item" @click="activeNav = 'mesrdv'">
                <div class="qi-icon" style="background:#fdf4ff;color:#a21caf">
                  <svg width="20" height="20" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2"/></svg>
                </div>
                <div style="flex:1">
                  <p class="qi-title">Historique des RDV</p>
                  <p class="qi-sub">{{ allRdv.length }} rendez-vous au total</p>
                </div>
                <svg width="14" height="14" fill="none" stroke="#cbd5e1" stroke-width="2.5" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" d="M9 5l7 7-7 7"/></svg>
              </button>
            </div>
          </div>

        </div>

        <!-- Historique -->
        <div class="panel">
          <div class="panel-header">
            <h4>Historique récent</h4>
            <button class="link-btn" @click="activeNav = 'mesrdv'">Voir tout →</button>
          </div>
          <div class="hist-list" v-if="recentRdv.length > 0">
            <div v-for="r in recentRdv" :key="r.id" class="hist-row">
              <div class="hist-avatar" :style="{ background: r.color }">{{ r.initials }}</div>
              <div class="hist-info">
                <p class="hist-doc">Dr. {{ r.doctor }}</p>
                <p class="hist-meta">{{ r.specialty }} · {{ r.date }}</p>
              </div>
              <span :class="['hist-badge', `hbadge-${r.status}`]">{{ r.statusLabel }}</span>
            </div>
          </div>
          <div v-else class="hist-empty">
            <p>Aucun historique — vos rendez-vous apparaîtront ici.</p>
          </div>
        </div>

      </div>

      <!-- ══ PAGE: Prendre RDV — Wizard ══ -->
      <div v-else-if="activeNav === 'rdv'" class="page-content">

        <!-- Si RDV confirmé -->
        <div v-if="rdv.confirmed" class="confirmed-full">
          <div class="confirmed-icon-lg">
            <svg width="44" height="44" fill="none" stroke="#22c55e" stroke-width="2.5" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" d="M5 13l4 4L19 7"/></svg>
          </div>
          <h3>Rendez-vous confirmé !</h3>
          <div class="confirmed-summary">
            <div class="cs-row"><span>Domaine</span><strong>{{ rdv.domaine?.label }}</strong></div>
            <div class="cs-row"><span>Ville</span><strong>{{ rdv.ville?.name }}</strong></div>
            <div class="cs-row"><span>Secteur</span><strong>{{ rdv.secteur?.name }}</strong></div>
            <div class="cs-row"><span>Cabinet</span><strong>{{ rdv.cabinet?.name }}</strong></div>
            <div class="cs-row"><span>Médecin</span><strong>Dr. {{ rdv.medecin?.name }}</strong></div>
            <div class="cs-row"><span>Date & heure</span><strong>{{ rdv.form.date }} à {{ rdv.form.heure }}</strong></div>
          </div>
          <button class="btn-primary" style="max-width:260px;margin:0 auto;" @click="activeNav = 'mesrdv'">Voir mes rendez-vous</button>
          <button class="btn-secondary-full" @click="resetRdv">Prendre un autre RDV</button>
        </div>

        <!-- Wizard -->
        <div v-else>
          <!-- Step bar -->
          <div class="wizard-steps">
            <div v-for="(step, i) in wizardSteps" :key="step.key"
              :class="['wizard-step', { active: rdv.step === i + 1, done: rdv.step > i + 1 }]">
              <div class="ws-icon" v-html="step.icon"></div>
              <span class="ws-label">{{ step.label }}</span>
              <div v-if="i < wizardSteps.length - 1" class="ws-line" :class="{ filled: rdv.step > i + 1 }"></div>
            </div>
          </div>

          <!-- Step 1 — Domaine médical -->
          <div v-if="rdv.step === 1" class="step-body">

            <!-- Hero -->
            <div class="d1-hero">
              <div class="d1-hero-left">
                <div class="d1-hero-tag">⚕️ Prise de rendez-vous</div>
                <h2 class="d1-hero-title">Choisissez votre<br><span>spécialité médicale</span></h2>
                <p class="d1-hero-sub">{{ domaines.length }} spécialités · 60 villes · Maroc</p>
              </div>
              <div class="d1-hero-right">
                <div class="d1-stat"><p class="d1-stat-num">{{ filteredDomaines.length }}</p><p class="d1-stat-lbl">disponibles</p></div>
                <div class="d1-stat-sep"></div>
                <div class="d1-stat"><p class="d1-stat-num">60</p><p class="d1-stat-lbl">villes</p></div>
              </div>
            </div>

            <!-- Recherche -->
            <div class="d1-search-wrap">
              <svg width="20" height="20" fill="none" stroke="#94a3b8" stroke-width="2" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"/></svg>
              <input v-model="domaineSearch" type="text" placeholder="Cardiologie, Dentaire, Pédiatrie…" class="d1-search-input"/>
              <button v-if="domaineSearch" @click="domaineSearch=''" class="d1-search-clear">✕</button>
            </div>

            <!-- Filtres -->
            <div class="d1-cats">
              <button v-for="c in domaineCategories" :key="c"
                :class="['d1-cat', { active: domaineFilter === c }]"
                :style="domaineFilter === c
                  ? `background:${catColor(c)};border-color:${catColor(c)};color:#fff;box-shadow:0 4px 14px ${catColor(c)}44`
                  : `border-color:#e2e8f0;color:#64748b`"
                @click="domaineFilter = c">
                {{ c === 'Toutes' ? 'Toutes' : c }}
              </button>
            </div>

            <!-- Grille portrait -->
            <div class="d1-grid">
              <button v-for="d in filteredDomaines" :key="d.key"
                :class="['d1-card', { selected: rdv.domaine?.key === d.key }]"
                :style="rdv.domaine?.key === d.key
                  ? `border-color:${catColor(d.categorie)};box-shadow:0 0 0 3px ${catColor(d.categorie)}28`
                  : ''"
                @click="rdv.domaine = d; rdv.step = 2">
                <div class="d1-card-top" :style="`background:linear-gradient(135deg,${catColor(d.categorie)}22,${catColor(d.categorie)}08)`">
                  <div class="d1-card-emoji-wrap" :style="`background:${catBg(d.categorie)};border:2px solid ${catColor(d.categorie)}22`">
                    <span class="d1-card-emoji">{{ d.emoji }}</span>
                  </div>
                </div>
                <div class="d1-card-body">
                  <p class="d1-card-name">{{ d.label }}</p>
                  <span class="d1-card-badge" :style="`background:${catColor(d.categorie)}12;color:${catColor(d.categorie)}`">{{ d.categorie }}</span>
                </div>
                <div class="d1-card-footer" :style="`background:${catColor(d.categorie)}08`">
                  <span :style="`color:${catColor(d.categorie)};font-size:11px;font-weight:700`">Consulter →</span>
                </div>
              </button>
            </div>

            <div v-if="filteredDomaines.length === 0" class="empty-state">
              <svg width="44" height="44" fill="none" stroke="#cbd5e1" stroke-width="1.5" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" d="M9.172 16.172a4 4 0 015.656 0M9 10h.01M15 10h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"/></svg>
              <p>Aucun résultat pour <strong>"{{ domaineSearch }}"</strong></p>
              <button @click="domaineSearch=''; domaineFilter='Toutes'" class="btn-link">Réinitialiser</button>
            </div>
          </div>

          <!-- Step 2 — Ville -->
          <div v-else-if="rdv.step === 2" class="step-body">
            <div class="step-recap">
              <span class="recap-pill">{{ rdv.domaine?.emoji }} {{ rdv.domaine?.label }}</span>
            </div>
            <p class="step-hint">Dans quelle ville souhaitez-vous consulter ?</p>

            <!-- Recherche ville -->
            <div class="search-bar" style="margin-bottom:16px;">
              <svg width="16" height="16" fill="none" stroke="#94a3b8" stroke-width="2" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"/></svg>
              <input v-model="villeSearch" type="text" placeholder="Rechercher une ville…"/>
            </div>

            <!-- Filtre région -->
            <div class="specialty-scroll" style="margin-bottom:16px;">
              <button v-for="r in regions" :key="r"
                :class="['specialty-pill', { active: regionFilter === r }]"
                @click="regionFilter = r">
                {{ r }}
              </button>
            </div>

            <div class="villes-grid">
              <button v-for="v in filteredVilles" :key="v.key"
                :class="['ville-card', { selected: rdv.ville?.key === v.key }]"
                @click="rdv.ville = v; rdv.secteur = null; rdv.step = 3">
                <span class="ville-flag">{{ v.flag }}</span>
                <span class="ville-name">{{ v.name }}</span>
                <span class="ville-count">{{ getSecteurs(v.key).length }} secteur{{ getSecteurs(v.key).length > 1 ? 's' : '' }}</span>
              </button>
            </div>
            <p v-if="filteredVilles.length === 0" class="empty-state">Aucune ville trouvée.</p>
          </div>

          <!-- Step 3 — Secteur -->
          <div v-else-if="rdv.step === 3" class="step-body">
            <div class="step-recap">
              <span class="recap-pill">{{ rdv.domaine?.emoji }} {{ rdv.domaine?.label }}</span>
              <span class="recap-sep">›</span>
              <span class="recap-pill">📍 {{ rdv.ville?.name }}</span>
            </div>

            <!-- Bandeau ville -->
            <div class="secteur-hero">
              <div class="secteur-hero-icon">🏙️</div>
              <div class="secteur-hero-text">
                <h3>{{ rdv.ville?.name }}</h3>
                <p>{{ getSecteurs(rdv.ville?.key).length }} quartiers · sélectionnez votre secteur</p>
              </div>
              <span class="secteur-hero-badge">
                {{ getSecteurs(rdv.ville?.key).filter(s => getCabinets(rdv.ville?.key, s.key).length > 0).length }}
                <small>actifs</small>
              </span>
            </div>

            <!-- Grille des secteurs -->
            <div class="secteurs-grid">
              <button
                v-for="s in getSecteurs(rdv.ville?.key)" :key="s.key"
                :class="['secteur-card', {
                  selected:    rdv.secteur?.key === s.key,
                  unavailable: getCabinets(rdv.ville?.key, s.key).length === 0
                }]"
                :disabled="getCabinets(rdv.ville?.key, s.key).length === 0"
                @click="rdv.secteur = s; rdv.step = 4">

                <div class="secteur-icon-wrap">
                  <svg width="20" height="20" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" d="M17.657 16.657L13.414 20.9a1.998 1.998 0 01-2.827 0l-4.244-4.243a8 8 0 1111.314 0z"/>
                    <path stroke-linecap="round" stroke-linejoin="round" d="M15 11a3 3 0 11-6 0 3 3 0 016 0z"/>
                  </svg>
                </div>

                <div class="secteur-body">
                  <p class="secteur-name">{{ s.name }}</p>
                  <p class="secteur-avail" v-if="getCabinets(rdv.ville?.key, s.key).length > 0">
                    {{ getCabinets(rdv.ville?.key, s.key).length }}
                    cabinet{{ getCabinets(rdv.ville?.key, s.key).length > 1 ? 's' : '' }} disponible{{ getCabinets(rdv.ville?.key, s.key).length > 1 ? 's' : '' }}
                  </p>
                  <p class="secteur-none" v-else>Aucun cabinet</p>
                </div>

                <svg v-if="getCabinets(rdv.ville?.key, s.key).length > 0"
                  class="secteur-chevron" width="15" height="15" fill="none" stroke="currentColor" stroke-width="2.5" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" d="M9 5l7 7-7 7"/>
                </svg>
              </button>
            </div>

            <div class="step-footer" style="margin-top:20px;">
              <button class="btn-back" @click="rdv.step = 2">← Retour</button>
            </div>
          </div>

          <!-- Step 4 — Cabinet -->
          <div v-else-if="rdv.step === 4" class="step-body">
            <div class="step-recap">
              <span class="recap-pill">{{ rdv.domaine?.emoji }} {{ rdv.domaine?.label }}</span>
              <span class="recap-sep">›</span>
              <span class="recap-pill">📍 {{ rdv.ville?.name }}</span>
              <span class="recap-sep">›</span>
              <span class="recap-pill">🏘️ {{ rdv.secteur?.name }}</span>
            </div>
            <p class="step-hint">Choisissez un établissement</p>
            <div class="cabinets-list">
              <button v-for="c in getCabinets(rdv.ville?.key, rdv.secteur?.key)" :key="c.id"
                :class="['cabinet-card', { selected: rdv.cabinet?.id === c.id }]"
                @click="rdv.cabinet = c; rdv.step = 5">
                <div class="cabinet-icon">
                  <svg width="22" height="22" fill="none" stroke="currentColor" stroke-width="1.8" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" d="M19 21V5a2 2 0 00-2-2H7a2 2 0 00-2 2v16m14 0h2m-2 0h-5m-9 0H3m2 0h5M9 7h1m-1 4h1m4-4h1m-1 4h1m-5 10v-5a1 1 0 011-1h2a1 1 0 011 1v5m-4 0h4"/></svg>
                </div>
                <div class="cabinet-info">
                  <h5>{{ c.name }}</h5>
                  <p><svg width="12" height="12" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" d="M17.657 16.657L13.414 20.9a1.998 1.998 0 01-2.827 0l-4.244-4.243a8 8 0 1111.314 0z"/></svg>{{ c.adresse }}</p>
                  <p><svg width="12" height="12" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" d="M3 5a2 2 0 012-2h3.28a1 1 0 01.948.684l1.498 4.493a1 1 0 01-.502 1.21l-2.257 1.13a11.042 11.042 0 005.516 5.516l1.13-2.257a1 1 0 011.21-.502l4.493 1.498a1 1 0 01.684.949V19a2 2 0 01-2 2h-1C9.716 21 3 14.284 3 6V5z"/></svg>{{ c.telephone }}</p>
                </div>
                <div class="cabinet-arrow">
                  <svg width="18" height="18" fill="none" stroke="currentColor" stroke-width="2.5" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" d="M9 5l7 7-7 7"/></svg>
                </div>
              </button>
              <p v-if="getCabinets(rdv.ville?.key, rdv.secteur?.key).length === 0" class="empty-state">Aucun cabinet dans ce secteur.</p>
            </div>
            <div class="step-footer">
              <button class="btn-back" @click="rdv.step = 3">← Retour</button>
            </div>
          </div>

          <!-- Step 5 — Médecin -->
          <div v-else-if="rdv.step === 5" class="step-body">
            <div class="step-recap">
              <span class="recap-pill">{{ rdv.domaine?.emoji }} {{ rdv.domaine?.label }}</span>
              <span class="recap-sep">›</span>
              <span class="recap-pill">📍 {{ rdv.ville?.name }}</span>
              <span class="recap-sep">›</span>
              <span class="recap-pill">🏘️ {{ rdv.secteur?.name }}</span>
              <span class="recap-sep">›</span>
              <span class="recap-pill">🏥 {{ rdv.cabinet?.name }}</span>
            </div>
            <p class="step-hint">Choisissez votre médecin</p>
            <div class="medecins-list">
              <button v-for="m in getMedecins(rdv.cabinet?.id)" :key="m.id"
                :class="['medecin-card', { selected: rdv.medecin?.id === m.id }]"
                @click="rdv.medecin = m">
                <div class="medecin-avatar" :style="{ background: m.color }">{{ m.initials }}</div>
                <div class="medecin-info">
                  <h5>Dr. {{ m.name }}</h5>
                  <p class="medecin-spec">{{ m.spec }}</p>
                  <div class="medecin-meta">
                    <span>
                      <svg width="12" height="12" fill="#f59e0b" viewBox="0 0 24 24"><path d="M11.049 2.927c.3-.921 1.603-.921 1.902 0l1.519 4.674a1 1 0 00.95.69h4.915c.969 0 1.371 1.24.588 1.81l-3.976 2.888a1 1 0 00-.363 1.118l1.518 4.674c.3.922-.755 1.688-1.538 1.118l-3.976-2.888a1 1 0 00-1.176 0l-3.976 2.888c-.783.57-1.838-.197-1.538-1.118l1.518-4.674a1 1 0 00-.363-1.118l-3.976-2.888c-.784-.57-.38-1.81.588-1.81h4.914a1 1 0 00.951-.69l1.519-4.674z"/></svg>
                      {{ m.rating }}
                    </span>
                    <span>{{ m.experience }} ans d'expérience</span>
                    <span class="tarif-badge">{{ m.tarif }} DH</span>
                  </div>
                </div>
                <div v-if="rdv.medecin?.id === m.id" class="check-mark">
                  <svg width="18" height="18" fill="none" stroke="white" stroke-width="3" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" d="M5 13l4 4L19 7"/></svg>
                </div>
              </button>
            </div>
            <div class="step-footer">
              <button class="btn-back" @click="rdv.step = 4">← Retour</button>
              <button class="btn-primary step-next" :disabled="!rdv.medecin" @click="rdv.step = 6">
                Suivant →
              </button>
            </div>
          </div>

          <!-- Step 6 — Confirmer -->
          <div v-else-if="rdv.step === 6" class="step-body">
            <div class="step-recap">
              <span class="recap-pill">{{ rdv.domaine?.emoji }} {{ rdv.domaine?.label }}</span>
              <span class="recap-sep">›</span>
              <span class="recap-pill">📍 {{ rdv.ville?.name }}</span>
              <span class="recap-sep">›</span>
              <span class="recap-pill">🏘️ {{ rdv.secteur?.name }}</span>
              <span class="recap-sep">›</span>
              <span class="recap-pill">🏥 {{ rdv.cabinet?.name }}</span>
              <span class="recap-sep">›</span>
              <span class="recap-pill">👨‍⚕️ Dr. {{ rdv.medecin?.name }}</span>
            </div>

            <div class="confirm-grid">
              <!-- Créneaux -->
              <div class="confirm-section">
                <h4 class="confirm-section-title">
                  <svg width="16" height="16" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z"/></svg>
                  Choisir une date
                </h4>
                <div class="date-strip">
                  <button v-for="d in bookingDates" :key="d.key"
                    :class="['date-pill', { active: rdv.form.date === d.key }]"
                    @click="rdv.form.date = d.key; rdv.form.heure = ''">
                    <span class="date-pill-day">{{ d.dayName }}</span>
                    <span class="date-pill-num">{{ d.dayNum }}</span>
                    <span class="date-pill-month">{{ d.month }}</span>
                  </button>
                </div>

                <div v-if="rdv.form.date">
                  <div v-for="period in slotPeriods" :key="period.key">
                    <p class="period-label"><span>{{ period.emoji }}</span> {{ period.label }}</p>
                    <div class="slots-grid">
                      <button v-for="slot in getSlots(period.key)" :key="slot.time"
                        :class="['slot-btn', { active: rdv.form.heure === slot.time, taken: slot.taken }]"
                        :disabled="slot.taken"
                        @click="rdv.form.heure = slot.time">
                        {{ slot.time }}
                      </button>
                    </div>
                  </div>
                </div>
              </div>

              <!-- Motif + récap -->
              <div class="confirm-section">
                <h4 class="confirm-section-title">
                  <svg width="16" height="16" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z"/></svg>
                  Motif de consultation
                </h4>
                <textarea v-model="rdv.form.motif" class="motif-input" rows="3"
                  placeholder="Ex: Douleur, contrôle annuel, bilan…"></textarea>

                <div class="booking-summary" v-if="rdv.form.date && rdv.form.heure">
                  <p class="summary-title">Récapitulatif</p>
                  <div class="summary-row"><span>Médecin</span><strong>Dr. {{ rdv.medecin?.name }}</strong></div>
                  <div class="summary-row"><span>Spécialité</span><strong>{{ rdv.domaine?.label }}</strong></div>
                  <div class="summary-row"><span>Cabinet</span><strong>{{ rdv.cabinet?.name }}</strong></div>
                  <div class="summary-row"><span>Ville</span><strong>{{ rdv.ville?.name }}</strong></div>
                  <div class="summary-row"><span>Date</span><strong>{{ rdv.form.date }}</strong></div>
                  <div class="summary-row"><span>Heure</span><strong>{{ rdv.form.heure }}</strong></div>
                  <div class="summary-row"><span>Tarif</span><strong class="tarif-highlight">{{ rdv.medecin?.tarif }} DH</strong></div>
                </div>
              </div>
            </div>

            <div class="step-footer" style="margin-top:24px;">
              <button class="btn-back" @click="rdv.step = 5">← Retour</button>
              <button class="btn-primary step-next"
                :disabled="!rdv.form.date || !rdv.form.heure"
                @click="confirmRdv()">
                ✓ Confirmer le rendez-vous
              </button>
            </div>
          </div>

        </div>
      </div>

      <!-- ══ PAGE: Mes RDV ══ -->
      <div v-else-if="activeNav === 'mesrdv'" class="page-content">

        <!-- État vide -->
        <div v-if="allRdv.length === 0" class="rdv-empty">
          <div class="rdv-empty-icon">
            <svg width="40" height="40" fill="none" stroke="#94a3b8" stroke-width="1.5" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z"/></svg>
          </div>
          <h3>Aucun rendez-vous</h3>
          <p>Vous n'avez pas encore de rendez-vous. Prenez votre premier RDV en ligne !</p>
          <button class="btn-submit-sm" @click="activeNav = 'rdv'; resetRdv()">Prendre un RDV</button>
        </div>

        <div v-else style="display:flex;flex-direction:column;gap:14px;">
          <div v-for="r in allRdv" :key="r.id" class="rdv-full-card">
            <div class="rfc-status-bar" :class="`rfc-${r.status}`"></div>
            <div class="rfc-body">
              <div class="rfc-avatar" :style="{ background: r.color }">{{ r.initials }}</div>
              <div class="rfc-info">
                <p class="rfc-doctor">Dr. {{ r.doctor }}</p>
                <p class="rfc-spec">{{ r.specialty }}</p>
                <div class="rfc-meta-row">
                  <span class="rfc-meta-chip">
                    <svg width="12" height="12" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z"/></svg>
                    {{ r.date }}
                  </span>
                  <span class="rfc-meta-chip">
                    <svg width="12" height="12" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z"/></svg>
                    {{ r.time }}
                  </span>
                  <span class="rfc-meta-chip">
                    <svg width="12" height="12" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" d="M17.657 16.657L13.414 20.9a1.998 1.998 0 01-2.827 0l-4.244-4.243a8 8 0 1111.314 0z"/><path stroke-linecap="round" stroke-linejoin="round" d="M15 11a3 3 0 11-6 0 3 3 0 016 0z"/></svg>
                    {{ r.lieu }}
                  </span>
                </div>
              </div>
              <div class="rfc-right">
                <span :class="['rdv-badge', `badge-${r.status}`]">{{ r.statusLabel }}</span>
                <button v-if="r.status === 'planned'" class="btn-cancel">Annuler</button>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- ══ PAGE: Dossier ══ -->
      <div v-else-if="activeNav === 'dossier'" class="page-content">
        <div class="dossier-hero">
          <div class="dossier-icon">
            <svg width="36" height="36" fill="none" stroke="#2563eb" stroke-width="1.5" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z"/></svg>
          </div>
          <div>
            <h3>Dossier médical de {{ userName }}</h3>
            <p>Créé automatiquement · Mis à jour par votre médecin</p>
          </div>
          <span class="pill-green">● Actif</span>
        </div>
        <div class="home-2col" style="margin-top:20px">
          <div class="panel" v-for="section in dossierSections" :key="section.title">
            <div class="panel-header">
              <h4>{{ section.title }}</h4>
              <span style="font-size:22px">{{ section.icon }}</span>
            </div>
            <p style="font-size:13px;color:#94a3b8;margin:0">{{ section.desc }}</p>
          </div>
        </div>
      </div>

    </main>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const user = JSON.parse(localStorage.getItem('user') || '{}')
const userName    = computed(() => user.prenom ? `${user.prenom} ${user.nom || ''}`.trim() : user.email || 'Patient')
const userEmail   = computed(() => user.email || '')
const userInitials = computed(() => {
  if (user.prenom && user.nom) return (user.prenom[0] + user.nom[0]).toUpperCase()
  return userEmail.value[0]?.toUpperCase() || 'P'
})

function logout() {
  localStorage.removeItem('token')
  localStorage.removeItem('user')
  router.push('/login')
}

// ── Nav ───────────────────────────────────────────────────────
const activeNav   = ref('home')
const sidebarOpen = ref(false)

const navItems = [
  { key: 'home',    label: 'Accueil',         icon: '<svg width="18" height="18" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" d="M3 12l2-2m0 0l7-7 7 7M5 10v10a1 1 0 001 1h3m10-11l2 2m-2-2v10a1 1 0 01-1 1h-3m-6 0a1 1 0 001-1v-4a1 1 0 011-1h2a1 1 0 011 1v4a1 1 0 001 1m-6 0h6"/></svg>' },
  { key: 'rdv',     label: 'Prendre un RDV',  icon: '<svg width="18" height="18" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z"/></svg>' },
  { key: 'mesrdv',  label: 'Mes rendez-vous', icon: '<svg width="18" height="18" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2"/></svg>' },
  { key: 'dossier', label: 'Mon dossier',      icon: '<svg width="18" height="18" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z"/></svg>' },
]

const todayStr = computed(() => new Date().toLocaleDateString('fr-MA', { weekday: 'long', day: 'numeric', month: 'long', year: 'numeric' }))

const allRdv = ref([])

// ── Prochain RDV + Historique calculés ──────────────────────
const nextRdv   = computed(() => allRdv.value.find(r => r.status === 'planned') || null)
const recentRdv = computed(() => allRdv.value.slice(0, 3))

// ── Stats calculées ──────────────────────────────────────────
const stats = computed(() => {
  const planned  = allRdv.value.filter(r => r.status === 'planned').length
  const doctors  = new Set(allRdv.value.map(r => r.doctor)).size
  const next     = nextRdv.value
  const nextDate = next ? next.date?.split(' ').slice(0,2).join(' ') : '—'
  return [
    { label: 'RDV planifiés',      value: String(planned), bg: '#eff6ff', icon: '<svg width="20" height="20" fill="none" stroke="#2563eb" stroke-width="2" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z"/></svg>' },
    { label: 'Médecins consultés', value: String(doctors), bg: '#f0fdf4', icon: '<svg width="20" height="20" fill="none" stroke="#16a34a" stroke-width="2" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z"/></svg>' },
    { label: 'Prochain RDV',       value: nextDate,        bg: '#fff7ed', icon: '<svg width="20" height="20" fill="none" stroke="#ea580c" stroke-width="2" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z"/></svg>' },
    { label: 'Dossier médical',    value: 'Actif',         bg: '#fdf4ff', icon: '<svg width="20" height="20" fill="none" stroke="#a21caf" stroke-width="2" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z"/></svg>' },
  ]
})

const avatarColors = ['#2563eb','#7c3aed','#0891b2','#be185d','#059669','#dc2626','#d97706','#0d9488']

function confirmRdv() {
  const m = rdv.value.medecin
  const name = m?.name || 'Médecin'
  const initials = name.split(' ').map(w => w[0]).join('').toUpperCase().slice(0,2)
  const color = avatarColors[allRdv.value.length % avatarColors.length]
  allRdv.value.unshift({
    id:      Date.now(),
    doctor:  name,
    specialty: rdv.value.domaine?.label || '',
    date:    rdv.value.form.date,
    time:    rdv.value.form.heure,
    lieu:    `${rdv.value.cabinet?.name || ''}, ${rdv.value.ville?.name || ''}`,
    status:  'planned',
    statusLabel: 'Planifié',
    initials,
    color,
  })
  rdv.value.confirmed = true
}

const dossierSections = [
  { title:'Consultations',  icon:'🩺', desc:'Vos consultations médicales seront enregistrées ici par votre médecin.' },
  { title:'Ordonnances',    icon:'💊', desc:'Retrouvez toutes vos prescriptions médicales et médicaments.' },
  { title:'Radiographies',  icon:'📡', desc:'Imagerie médicale : radios, IRM, scanners et résultats.' },
  { title:'Analyses',       icon:'🔬', desc:'Résultats d\'analyses biologiques et bilans de santé.' },
  { title:'Soins',          icon:'🩹', desc:'Historique des soins et traitements effectués.' },
  { title:'Notes cliniques',icon:'📋', desc:'Notes et observations laissées par vos médecins.' },
]

// ── Wizard RDV ───────────────────────────────────────────────
const rdv = ref({ step: 1, domaine: null, ville: null, secteur: null, cabinet: null, medecin: null, form: { date: '', heure: '', motif: '' }, confirmed: false })

function resetRdv() {
  rdv.value = { step: 1, domaine: null, ville: null, secteur: null, cabinet: null, medecin: null, form: { date: '', heure: '', motif: '' }, confirmed: false }
}

const wizardSteps = [
  { key: 'domaine',  label: 'Domaine',  icon: '<svg width="16" height="16" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2"/></svg>' },
  { key: 'ville',    label: 'Ville',    icon: '<svg width="16" height="16" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" d="M17.657 16.657L13.414 20.9a1.998 1.998 0 01-2.827 0l-4.244-4.243a8 8 0 1111.314 0z"/></svg>' },
  { key: 'secteur',  label: 'Secteur',  icon: '<svg width="16" height="16" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" d="M3 12l2-2m0 0l7-7 7 7M5 10v10a1 1 0 001 1h3m10-11l2 2m-2-2v10a1 1 0 01-1 1h-3m-6 0a1 1 0 001-1v-4a1 1 0 011-1h2a1 1 0 011 1v4a1 1 0 001 1m-6 0h6"/></svg>' },
  { key: 'cabinet',  label: 'Cabinet',  icon: '<svg width="16" height="16" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" d="M19 21V5a2 2 0 00-2-2H7a2 2 0 00-2 2v16m14 0h2m-2 0h-5m-9 0H3m2 0h5M9 7h1m-1 4h1m4-4h1m-1 4h1m-5 10v-5a1 1 0 011-1h2a1 1 0 011 1v5m-4 0h4"/></svg>' },
  { key: 'medecin',  label: 'Médecin',  icon: '<svg width="16" height="16" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z"/></svg>' },
  { key: 'confirmer',label: 'Confirmer',icon: '<svg width="16" height="16" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z"/></svg>' },
]

// ── Domaines médicaux (liste complète) ───────────────────────
const domaines = [
  // Médecine générale
  { key: 'generaliste',       label: 'Médecine générale',    emoji: '🩺', categorie: 'Médecine générale' },
  { key: 'urgences',          label: 'Médecine d\'urgence',  emoji: '🚑', categorie: 'Médecine générale' },
  { key: 'medecine_interne',  label: 'Médecine interne',     emoji: '🏥', categorie: 'Médecine générale' },
  { key: 'medecine_travail',  label: 'Médecine du travail',  emoji: '💼', categorie: 'Médecine générale' },
  { key: 'geriatrie',         label: 'Gériatrie',            emoji: '👴', categorie: 'Médecine générale' },
  // Cardiologie & Vasculaire
  { key: 'cardiologie',       label: 'Cardiologie',          emoji: '❤️', categorie: 'Cardio & Vasculaire' },
  { key: 'chir_vasculaire',   label: 'Chirurgie vasculaire', emoji: '🫀', categorie: 'Cardio & Vasculaire' },
  { key: 'angiologie',        label: 'Angiologie',           emoji: '🩸', categorie: 'Cardio & Vasculaire' },
  // Neurologie & Psychiatrie
  { key: 'neurologie',        label: 'Neurologie',           emoji: '🧠', categorie: 'Neurologie & Psy' },
  { key: 'neurochirurgie',    label: 'Neurochirurgie',       emoji: '🔬', categorie: 'Neurologie & Psy' },
  { key: 'psychiatrie',       label: 'Psychiatrie',          emoji: '🧘', categorie: 'Neurologie & Psy' },
  { key: 'psychologie',       label: 'Psychologie',          emoji: '💭', categorie: 'Neurologie & Psy' },
  // Pédiatrie & Femme
  { key: 'pediatrie',         label: 'Pédiatrie',            emoji: '👶', categorie: 'Pédiatrie & Femme' },
  { key: 'neonatologie',      label: 'Néonatologie',         emoji: '🍼', categorie: 'Pédiatrie & Femme' },
  { key: 'gynecologie',       label: 'Gynécologie',          emoji: '🌸', categorie: 'Pédiatrie & Femme' },
  { key: 'obstetrique',       label: 'Obstétrique',          emoji: '🤰', categorie: 'Pédiatrie & Femme' },
  // Digestif & Rénal
  { key: 'gastro',            label: 'Gastro-entérologie',   emoji: '🫃', categorie: 'Digestif & Rénal' },
  { key: 'hepatologie',       label: 'Hépatologie',          emoji: '🫁', categorie: 'Digestif & Rénal' },
  { key: 'urologie',          label: 'Urologie',             emoji: '🫘', categorie: 'Digestif & Rénal' },
  { key: 'nephrologie',       label: 'Néphrologie',          emoji: '💊', categorie: 'Digestif & Rénal' },
  { key: 'proctologie',       label: 'Proctologie',          emoji: '🔭', categorie: 'Digestif & Rénal' },
  // Os & Articulations
  { key: 'orthopedie',        label: 'Orthopédie',           emoji: '🦴', categorie: 'Os & Articulations' },
  { key: 'rhumatologie',      label: 'Rhumatologie',         emoji: '🦿', categorie: 'Os & Articulations' },
  { key: 'traumatologie',     label: 'Traumatologie',        emoji: '🏋️', categorie: 'Os & Articulations' },
  // ORL, Yeux & Peau
  { key: 'orl',               label: 'ORL',                  emoji: '👂', categorie: 'ORL, Yeux & Peau' },
  { key: 'ophtalmologie',     label: 'Ophtalmologie',        emoji: '👁️', categorie: 'ORL, Yeux & Peau' },
  { key: 'dermatologie',      label: 'Dermatologie',         emoji: '🩹', categorie: 'ORL, Yeux & Peau' },
  { key: 'allergologie',      label: 'Allergologie',         emoji: '🤧', categorie: 'ORL, Yeux & Peau' },
  // Dentaire
  { key: 'dentaire',          label: 'Dentaire',             emoji: '🦷', categorie: 'Dentaire' },
  { key: 'orthodontie',       label: 'Orthodontie',          emoji: '😬', categorie: 'Dentaire' },
  { key: 'chir_dentaire',     label: 'Chirurgie dentaire',   emoji: '⚕️', categorie: 'Dentaire' },
  { key: 'implantologie',     label: 'Implantologie',        emoji: '🔩', categorie: 'Dentaire' },
  // Endocrine & Métabolique
  { key: 'endocrinologie',    label: 'Endocrinologie',       emoji: '⚗️', categorie: 'Endocrine & Métabo' },
  { key: 'diabetologie',      label: 'Diabétologie',         emoji: '💉', categorie: 'Endocrine & Métabo' },
  { key: 'nutrition',         label: 'Nutrition & Diététique', emoji: '🥗', categorie: 'Endocrine & Métabo' },
  // Respiratoire
  { key: 'pneumologie',       label: 'Pneumologie',          emoji: '🫁', categorie: 'Respiratoire' },
  { key: 'tabacologie',       label: 'Tabacologie',          emoji: '🚭', categorie: 'Respiratoire' },
  // Oncologie
  { key: 'oncologie',         label: 'Oncologie',            emoji: '🎗️', categorie: 'Oncologie' },
  { key: 'hematologie',       label: 'Hématologie',          emoji: '🩸', categorie: 'Oncologie' },
  { key: 'radiotherapie',     label: 'Radiothérapie',        emoji: '☢️', categorie: 'Oncologie' },
  // Imagerie & Biologie
  { key: 'radiologie',        label: 'Radiologie',           emoji: '📡', categorie: 'Imagerie & Biologie' },
  { key: 'biologie',          label: 'Biologie médicale',    emoji: '🔬', categorie: 'Imagerie & Biologie' },
  { key: 'medecine_physique', label: 'Médecine physique',    emoji: '💪', categorie: 'Imagerie & Biologie' },
]

// ── Recherche et filtre domaines ──────────────────────────────
const domaineSearch  = ref('')
const domaineFilter  = ref('Toutes')

const domaineCategories = computed(() => {
  const set = new Set(domaines.map(d => d.categorie))
  return ['Toutes', ...set]
})

const filteredDomaines = computed(() => {
  return domaines.filter(d => {
    const matchCat    = domaineFilter.value === 'Toutes' || d.categorie === domaineFilter.value
    const matchSearch = !domaineSearch.value || d.label.toLowerCase().includes(domaineSearch.value.toLowerCase())
    return matchCat && matchSearch
  })
})

// ── Villes marocaines (toutes les villes) ────────────────────
const villes = [
  // Région Casablanca-Settat
  { key: 'casa',         name: 'Casablanca',           flag: '🏙️', region: 'Casablanca-Settat' },
  { key: 'mohammedia',   name: 'Mohammedia',           flag: '🏭', region: 'Casablanca-Settat' },
  { key: 'settat',       name: 'Settat',               flag: '🌾', region: 'Casablanca-Settat' },
  { key: 'berrechid',    name: 'Berrechid',            flag: '🏘️', region: 'Casablanca-Settat' },
  { key: 'benslimane',   name: 'Benslimane',           flag: '🌳', region: 'Casablanca-Settat' },
  { key: 'eljadida',     name: 'El Jadida',            flag: '🏰', region: 'Casablanca-Settat' },
  // Région Rabat-Salé-Kénitra
  { key: 'rabat',        name: 'Rabat',                flag: '🏛️', region: 'Rabat-Salé-Kénitra' },
  { key: 'sale',         name: 'Salé',                 flag: '🕍', region: 'Rabat-Salé-Kénitra' },
  { key: 'temara',       name: 'Témara',               flag: '🌆', region: 'Rabat-Salé-Kénitra' },
  { key: 'kenitra',      name: 'Kénitra',              flag: '🌿', region: 'Rabat-Salé-Kénitra' },
  { key: 'khemisset',    name: 'Khémisset',            flag: '🌻', region: 'Rabat-Salé-Kénitra' },
  { key: 'sidikacem',    name: 'Sidi Kacem',           flag: '🌵', region: 'Rabat-Salé-Kénitra' },
  { key: 'sidi_slimane', name: 'Sidi Slimane',         flag: '🌴', region: 'Rabat-Salé-Kénitra' },
  { key: 'soukarbaa',    name: 'Souk El Arbaa',        flag: '🛒', region: 'Rabat-Salé-Kénitra' },
  // Région Fès-Meknès
  { key: 'fes',          name: 'Fès',                  flag: '🏺', region: 'Fès-Meknès' },
  { key: 'meknes',       name: 'Meknès',               flag: '🦁', region: 'Fès-Meknès' },
  { key: 'taza',         name: 'Taza',                 flag: '⛰️', region: 'Fès-Meknès' },
  { key: 'sefrou',       name: 'Sefrou',               flag: '🍊', region: 'Fès-Meknès' },
  { key: 'ifrane',       name: 'Ifrane',               flag: '❄️', region: 'Fès-Meknès' },
  { key: 'azrou',        name: 'Azrou',                flag: '🌲', region: 'Fès-Meknès' },
  { key: 'khenifra',     name: 'Khénifra',             flag: '🏔️', region: 'Fès-Meknès' },
  // Région Tanger-Tétouan-Al Hoceïma
  { key: 'tanger',       name: 'Tanger',               flag: '⚓', region: 'Tanger-Tétouan-Al Hoceïma' },
  { key: 'tetouan',      name: 'Tétouan',              flag: '⛵', region: 'Tanger-Tétouan-Al Hoceïma' },
  { key: 'alhoceima',    name: 'Al Hoceïma',           flag: '🌊', region: 'Tanger-Tétouan-Al Hoceïma' },
  { key: 'chefchaouen',  name: 'Chefchaouen',          flag: '💙', region: 'Tanger-Tétouan-Al Hoceïma' },
  { key: 'larache',      name: 'Larache',              flag: '🏖️', region: 'Tanger-Tétouan-Al Hoceïma' },
  { key: 'mdiq',         name: "M'Diq",                flag: '🐟', region: 'Tanger-Tétouan-Al Hoceïma' },
  { key: 'fnideq',       name: 'Fnideq',               flag: '🛳️', region: 'Tanger-Tétouan-Al Hoceïma' },
  { key: 'ksar_kebir',   name: 'Ksar el-Kébir',        flag: '🏯', region: 'Tanger-Tétouan-Al Hoceïma' },
  // Région de l'Oriental
  { key: 'oujda',        name: 'Oujda',                flag: '🌄', region: "Oriental" },
  { key: 'nador',        name: 'Nador',                flag: '⚙️', region: "Oriental" },
  { key: 'berkane',      name: 'Berkane',              flag: '🍋', region: "Oriental" },
  { key: 'taourirt',     name: 'Taourirt',             flag: '🏜️', region: "Oriental" },
  { key: 'figuig',       name: 'Figuig',               flag: '🌴', region: "Oriental" },
  { key: 'driouch',      name: 'Driouch',              flag: '🗻', region: "Oriental" },
  // Région Marrakech-Safi
  { key: 'marrakech',    name: 'Marrakech',            flag: '🕌', region: 'Marrakech-Safi' },
  { key: 'safi',         name: 'Safi',                 flag: '🏺', region: 'Marrakech-Safi' },
  { key: 'essaouira',    name: 'Essaouira',            flag: '🎠', region: 'Marrakech-Safi' },
  { key: 'benguerir',    name: 'Ben Guerir',           flag: '⚗️', region: 'Marrakech-Safi' },
  { key: 'youssoufia',   name: 'Youssoufia',           flag: '⛏️', region: 'Marrakech-Safi' },
  { key: 'kelaa',        name: 'Kelaa des Sraghna',    flag: '🌹', region: 'Marrakech-Safi' },
  // Région Souss-Massa
  { key: 'agadir',       name: 'Agadir',               flag: '🏄', region: 'Souss-Massa' },
  { key: 'tiznit',       name: 'Tiznit',               flag: '🐪', region: 'Souss-Massa' },
  { key: 'taroudant',    name: 'Taroudant',            flag: '🏰', region: 'Souss-Massa' },
  { key: 'inezgane',     name: 'Inezgane',             flag: '🏗️', region: 'Souss-Massa' },
  { key: 'ouldteima',    name: 'Oulad Teima',          flag: '🌾', region: 'Souss-Massa' },
  // Région Béni Mellal-Khénifra
  { key: 'benmellal',    name: 'Béni Mellal',          flag: '🏞️', region: 'Béni Mellal-Khénifra' },
  { key: 'khouribga',    name: 'Khouribga',            flag: '⛏️', region: 'Béni Mellal-Khénifra' },
  { key: 'azilal',       name: 'Azilal',               flag: '🏔️', region: 'Béni Mellal-Khénifra' },
  { key: 'fquih',        name: 'Fquih Ben Salah',      flag: '🌿', region: 'Béni Mellal-Khénifra' },
  // Région Drâa-Tafilalet
  { key: 'errachidia',   name: 'Errachidia',           flag: '🏜️', region: 'Drâa-Tafilalet' },
  { key: 'ouarzazate',   name: 'Ouarzazate',           flag: '🎬', region: 'Drâa-Tafilalet' },
  { key: 'zagora',       name: 'Zagora',               flag: '🐫', region: 'Drâa-Tafilalet' },
  { key: 'midelt',       name: 'Midelt',               flag: '🍎', region: 'Drâa-Tafilalet' },
  { key: 'tinghir',      name: 'Tinghir',              flag: '🗺️', region: 'Drâa-Tafilalet' },
  // Région Guelmim-Oued Noun
  { key: 'guelmim',      name: 'Guelmim',              flag: '🌵', region: 'Guelmim-Oued Noun' },
  { key: 'tantan',       name: 'Tan-Tan',              flag: '🌬️', region: 'Guelmim-Oued Noun' },
  { key: 'sidiifni',     name: 'Sidi Ifni',            flag: '🌅', region: 'Guelmim-Oued Noun' },
  { key: 'tata',         name: 'Tata',                 flag: '🏔️', region: 'Guelmim-Oued Noun' },
  // Région Laâyoune-Sakia El Hamra
  { key: 'laayoune',     name: 'Laâyoune',             flag: '🌐', region: 'Laâyoune-Sakia El Hamra' },
  { key: 'tarfaya',      name: 'Tarfaya',              flag: '🏝️', region: 'Laâyoune-Sakia El Hamra' },
  { key: 'boujdour',     name: 'Boujdour',             flag: '🌊', region: 'Laâyoune-Sakia El Hamra' },
  // Région Dakhla-Oued Ed-Dahab
  { key: 'dakhla',       name: 'Dakhla',               flag: '🏄', region: 'Dakhla-Oued Ed-Dahab' },
]

// ── Cabinets (mock par ville) ─────────────────────────────────
const cabinetsData = [
  // Casablanca-Settat
  { id:1,   ville:'casa',         secteur:'maarif',       name:'Clinique Atlas',              adresse:'45 Bd Mohammed V, Maarif',            telephone:'05 22 40 12 34' },
  { id:2,   ville:'casa',         secteur:'maarif',       name:'Cabinet Médical Maarif',      adresse:'12 Rue Al Aaraar, Maarif',            telephone:'05 22 25 67 89' },
  { id:3,   ville:'casa',         secteur:'ain_sebaa',    name:'Polyclinique Nord-Ouest',     adresse:'7 Av. des FAR, Ain Sebaa',            telephone:'05 22 35 44 55' },
  { id:76,  ville:'casa',         secteur:'anfa',         name:'Cabinet Anfa Médical',        adresse:'Bd de la Corniche, Anfa',             telephone:'05 22 79 32 44' },
  { id:77,  ville:'casa',         secteur:'sidi_maarouf', name:'Clinique Sidi Maarouf',       adresse:'Technopark, Sidi Maarouf',            telephone:'05 22 58 71 20' },
  { id:78,  ville:'casa',         secteur:'hay_hassani',  name:'Cabinet Hay Hassani',         adresse:'Av. Hay Hassani, Casablanca',         telephone:'05 22 90 14 33' },
  { id:79,  ville:'casa',         secteur:'lissasfa',     name:'Clinique Lissasfa',           adresse:'Av. Lissasfa, Casablanca',            telephone:'05 22 96 55 11' },
  { id:4,   ville:'mohammedia',   secteur:'centre',       name:'Clinique Méditerranée',       adresse:'Bd Mohammed V, Mohammedia',           telephone:'05 23 32 14 56' },
  { id:5,   ville:'settat',       secteur:'centre',       name:'Cabinet Al Amal',             adresse:'Av. Hassan II, Settat',               telephone:'05 23 40 22 11' },
  { id:6,   ville:'berrechid',    secteur:'centre',       name:'Cabinet du Centre',           adresse:'Quartier Administratif, Berrechid',   telephone:'05 22 33 55 44' },
  { id:7,   ville:'benslimane',   secteur:'centre',       name:'Cabinet Santé Plus',          adresse:'Av. Mohammed VI, Benslimane',         telephone:'05 22 66 17 82' },
  { id:8,   ville:'eljadida',     secteur:'centre',       name:'Clinique El Jadida',          adresse:'Bd Zerktouni, El Jadida',             telephone:'05 23 35 42 10' },
  // Rabat-Salé-Kénitra
  { id:9,   ville:'rabat',        secteur:'agdal',        name:'Clinique Agdal',              adresse:'18 Av. Fal Ould Oumeir, Agdal',      telephone:'05 37 67 23 45' },
  { id:10,  ville:'rabat',        secteur:'hassan',       name:'Hôpital Ibn Sina',            adresse:'Av. Hassan II, Hassan',              telephone:'05 37 67 28 01' },
  { id:80,  ville:'rabat',        secteur:'hay_riad',     name:'Cabinet Hay Riad Santé',      adresse:'Av. Al Boraq, Hay Riad',             telephone:'05 37 71 44 22' },
  { id:81,  ville:'rabat',        secteur:'souissi',      name:'Clinique Souissi',            adresse:'Av. Mehdi Ben Barka, Souissi',       telephone:'05 37 75 20 18' },
  { id:11,  ville:'sale',         secteur:'bab_lamrissa', name:'Clinique Bab Lamrissa',       adresse:'Quartier Bab Lamrissa, Salé',        telephone:'05 37 88 45 67' },
  { id:82,  ville:'sale',         secteur:'hay_salam',    name:'Cabinet Hay Salam Salé',      adresse:'Av. Hay Salam, Salé',               telephone:'05 37 88 90 12' },
  { id:12,  ville:'temara',       secteur:'centre',       name:'Cabinet Témara Santé',        adresse:'Bd Bir Anzarane, Témara',            telephone:'05 37 74 32 18' },
  { id:13,  ville:'kenitra',      secteur:'centre',       name:'Cabinet du Parc',             adresse:'Av. du Parc, Kénitra',               telephone:'05 37 37 21 43' },
  { id:14,  ville:'khemisset',    secteur:'centre',       name:'Cabinet Al Ittifak',          adresse:'Av. Mohammed V, Khémisset',          telephone:'05 37 55 23 89' },
  { id:15,  ville:'sidikacem',    secteur:'centre',       name:'Cabinet Sidi Kacem',          adresse:'Rue de la Paix, Sidi Kacem',         telephone:'05 37 60 14 75' },
  { id:16,  ville:'sidi_slimane', secteur:'centre',       name:'Cabinet Al Wifak',            adresse:'Bd Hassan II, Sidi Slimane',         telephone:'05 35 60 34 21' },
  { id:17,  ville:'soukarbaa',    secteur:'centre',       name:'Cabinet Souk El Arbaa',       adresse:'Av. Principale, Souk El Arbaa',      telephone:'05 37 62 11 40' },
  // Fès-Meknès
  { id:18,  ville:'fes',          secteur:'narjiss',      name:'Clinique Al Farabi',          adresse:'Quartier Narjiss, Fès',              telephone:'05 35 65 44 21' },
  { id:19,  ville:'fes',          secteur:'ville_nouvelle',name:'Cabinet Vision Santé',       adresse:'Av. Hassan II, Fès-Ville Nouvelle',  telephone:'05 35 93 12 78' },
  { id:83,  ville:'fes',          secteur:'medina',       name:'Clinique Médina Fès',         adresse:'Bab Guissa, Médina, Fès',            telephone:'05 35 63 11 90' },
  { id:84,  ville:'fes',          secteur:'agdal',        name:'Cabinet Agdal Fès',           adresse:'Av. Allal El Fassi, Agdal',          telephone:'05 35 94 22 45' },
  { id:20,  ville:'meknes',       secteur:'ville_nouvelle',name:'Cabinet Central Meknès',     adresse:'Av. Hassan II, Meknès',              telephone:'05 35 52 34 56' },
  { id:21,  ville:'meknes',       secteur:'hamria',       name:'Clinique Ibn Rochd',          adresse:'Quartier Hamria, Meknès',            telephone:'05 35 51 20 30' },
  { id:22,  ville:'taza',         secteur:'centre',       name:'Cabinet Taza Médical',        adresse:'Av. Mohammed V, Taza',               telephone:'05 35 67 22 44' },
  { id:23,  ville:'sefrou',       secteur:'centre',       name:'Cabinet Al Karam',            adresse:'Centre-ville, Sefrou',               telephone:'05 35 56 11 33' },
  { id:24,  ville:'ifrane',       secteur:'centre',       name:'Cabinet de la Forêt',         adresse:'Av. de la Marche Verte, Ifrane',     telephone:'05 35 56 60 12' },
  { id:25,  ville:'azrou',        secteur:'centre',       name:'Cabinet Azrou Santé',         adresse:'Av. Hassan II, Azrou',               telephone:'05 35 56 25 78' },
  { id:26,  ville:'khenifra',     secteur:'moulay_ismail',name:'Clinique Oum Rabiaa',         adresse:'Bd Moulay Ismail, Khénifra',         telephone:'05 35 58 40 66' },
  // Tanger-Tétouan-Al Hoceïma
  { id:27,  ville:'tanger',       secteur:'ville_nouvelle',name:'Polyclinique Tanger',        adresse:'Bd Mohammed VI, Tanger',             telephone:'05 39 32 55 66' },
  { id:28,  ville:'tanger',       secteur:'malabata',     name:'Clinique Ibn Baja',           adresse:'Quartier Malabata, Tanger',          telephone:'05 39 94 37 21' },
  { id:29,  ville:'tetouan',      secteur:'ensanche',     name:'Clinique Tamuda',             adresse:'Av. Hassan II, Tétouan',             telephone:'05 39 96 18 75' },
  { id:30,  ville:'alhoceima',    secteur:'centre',       name:'Cabinet Al Hoceïma Médical',  adresse:'Av. Mohammed V, Al Hoceïma',         telephone:'05 39 98 23 55' },
  { id:31,  ville:'chefchaouen',  secteur:'medina',       name:'Cabinet Chaouen Santé',       adresse:'Place Outa el-Hammam, Chefchaouen',  telephone:'05 39 98 72 16' },
  { id:32,  ville:'larache',      secteur:'centre',       name:'Clinique Larache',            adresse:'Bd Mohammed V, Larache',             telephone:'05 39 91 14 89' },
  { id:33,  ville:'mdiq',         secteur:'centre',       name:"Cabinet M'Diq Médical",       adresse:"Av. de la Côte, M'Diq",             telephone:'05 39 97 52 33' },
  { id:34,  ville:'fnideq',       secteur:'centre',       name:'Cabinet Fnideq Santé',        adresse:'Av. Hassan II, Fnideq',              telephone:'05 39 66 23 10' },
  { id:35,  ville:'ksar_kebir',   secteur:'centre',       name:'Clinique Ksar',               adresse:'Bd Moulay Ismail, Ksar el-Kébir',   telephone:'05 39 90 41 77' },
  // Oriental
  { id:36,  ville:'oujda',        secteur:'hay_qods',     name:'Clinique Al Massira',         adresse:'Bd Derfoufi, Oujda',                 telephone:'05 36 70 11 22' },
  { id:37,  ville:'oujda',        secteur:'ville_nouvelle',name:'Clinique Oujda',             adresse:'Av. Mohammed V, Oujda',              telephone:'05 36 68 21 90' },
  { id:38,  ville:'nador',        secteur:'centre',       name:'Clinique Nador',              adresse:'Av. des FAR, Nador',                 telephone:'05 36 60 44 15' },
  { id:39,  ville:'berkane',      secteur:'centre',       name:'Cabinet Berkane Santé',       adresse:'Av. Mohammed V, Berkane',            telephone:'05 36 61 22 87' },
  { id:40,  ville:'taourirt',     secteur:'centre',       name:'Cabinet Al Amal',             adresse:'Bd Hassan II, Taourirt',             telephone:'05 36 69 13 45' },
  { id:41,  ville:'figuig',       secteur:'zenaga',       name:'Cabinet Figuig',              adresse:'Quartier Zenaga, Figuig',            telephone:'05 36 35 90 11' },
  { id:42,  ville:'driouch',      secteur:'centre',       name:'Cabinet Al Karama',           adresse:'Centre-ville, Driouch',              telephone:'05 36 58 30 20' },
  // Marrakech-Safi
  { id:43,  ville:'marrakech',    secteur:'gueliz',       name:'Clinique Marrakech',          adresse:'Bd Zerktouni, Guéliz',               telephone:'05 24 43 27 86' },
  { id:44,  ville:'marrakech',    secteur:'medina',       name:'Cabinet Alami',               adresse:'Rue Ibn Toumert, Médina',            telephone:'05 24 38 91 20' },
  { id:45,  ville:'safi',         secteur:'centre',       name:'Clinique Safi Médical',       adresse:'Av. Mohammed V, Safi',               telephone:'05 24 46 33 77' },
  { id:46,  ville:'essaouira',    secteur:'medina',       name:'Cabinet Mogador Santé',       adresse:'Av. de l\'Istiqlal, Essaouira',     telephone:'05 24 47 55 90' },
  { id:47,  ville:'benguerir',    secteur:'centre',       name:'Cabinet Ben Guerir',          adresse:'Av. Hassan II, Ben Guerir',          telephone:'05 24 59 24 11' },
  { id:48,  ville:'youssoufia',   secteur:'centre',       name:'Cabinet Youssoufia',          adresse:'Av. Mohammed VI, Youssoufia',        telephone:'05 24 55 22 34' },
  { id:49,  ville:'kelaa',        secteur:'centre',       name:'Clinique Kelaa',              adresse:'Bd Mohammed V, Kelaa des Sraghna',   telephone:'05 24 43 82 60' },
  // Souss-Massa
  { id:50,  ville:'agadir',       secteur:'talborjt',     name:'Clinique Souss',              adresse:'Av. du Prince Moulay Abdallah',      telephone:'05 28 84 26 30' },
  { id:51,  ville:'agadir',       secteur:'hay_mohammadi',name:'Clinique Agadir Sud',         adresse:'Bd du 20 Août, Agadir',              telephone:'05 28 82 14 55' },
  { id:52,  ville:'tiznit',       secteur:'centre',       name:'Cabinet Tiznit Médical',      adresse:'Av. Hassan II, Tiznit',              telephone:'05 28 60 33 12' },
  { id:53,  ville:'taroudant',    secteur:'centre',       name:'Clinique Taroudant',          adresse:'Av. Mohammed V, Taroudant',          telephone:'05 28 85 21 67' },
  { id:54,  ville:'inezgane',     secteur:'centre',       name:'Cabinet Inezgane Santé',      adresse:'Av. Bir Anzarane, Inezgane',         telephone:'05 28 83 70 45' },
  { id:55,  ville:'ouldteima',    secteur:'centre',       name:'Cabinet Oulad Teima',         adresse:'Centre-ville, Oulad Teima',          telephone:'05 28 87 11 30' },
  // Béni Mellal-Khénifra
  { id:56,  ville:'benmellal',    secteur:'centre',       name:'Clinique Béni Mellal',        adresse:'Bd Mohammed V, Béni Mellal',         telephone:'05 23 48 32 10' },
  { id:57,  ville:'benmellal',    secteur:'hay_ismailia', name:'Cabinet Al Hikma',            adresse:'Quartier Hay Ismaïlia, Béni Mellal', telephone:'05 23 43 90 25' },
  { id:58,  ville:'khouribga',    secteur:'centre',       name:'Clinique Khouribga',          adresse:'Av. des Phosphates, Khouribga',      telephone:'05 23 49 44 16' },
  { id:59,  ville:'azilal',       secteur:'centre',       name:'Cabinet Azilal',              adresse:'Av. Hassan II, Azilal',              telephone:'05 23 45 80 77' },
  { id:60,  ville:'fquih',        secteur:'centre',       name:'Cabinet Fquih Ben Salah',     adresse:'Bd Mohammed VI, Fquih Ben Salah',    telephone:'05 23 41 12 89' },
  // Drâa-Tafilalet
  { id:61,  ville:'errachidia',   secteur:'centre',       name:'Clinique Errachidia',         adresse:'Av. Moulay Ali Chérif, Errachidia',  telephone:'05 35 57 23 44' },
  { id:62,  ville:'ouarzazate',   secteur:'centre',       name:'Clinique Sud Médical',        adresse:'Av. Mohammed V, Ouarzazate',         telephone:'05 24 88 25 99' },
  { id:63,  ville:'zagora',       secteur:'centre',       name:'Cabinet Zagora Santé',        adresse:'Av. Hassan II, Zagora',              telephone:'05 24 84 74 16' },
  { id:64,  ville:'midelt',       secteur:'centre',       name:'Cabinet Midelt Médical',      adresse:'Av. Hassan II, Midelt',              telephone:'05 35 58 20 33' },
  { id:65,  ville:'tinghir',      secteur:'centre',       name:'Cabinet Gorges du Todgha',    adresse:'Av. Mohammed V, Tinghir',            telephone:'05 24 83 55 44' },
  // Guelmim-Oued Noun
  { id:66,  ville:'guelmim',      secteur:'centre',       name:'Clinique Guelmim',            adresse:'Av. Hassan II, Guelmim',             telephone:'05 28 77 21 30' },
  { id:67,  ville:'tantan',       secteur:'centre',       name:'Cabinet Tan-Tan Médical',     adresse:'Av. Mohammed V, Tan-Tan',            telephone:'05 28 87 63 18' },
  { id:68,  ville:'sidiifni',     secteur:'centre',       name:'Cabinet Sidi Ifni',           adresse:'Av. Mohammed VI, Sidi Ifni',         telephone:'05 28 78 50 12' },
  { id:69,  ville:'tata',         secteur:'centre',       name:'Cabinet Tata Santé',          adresse:'Centre-ville, Tata',                 telephone:'05 28 80 30 44' },
  // Laâyoune-Sakia El Hamra
  { id:70,  ville:'laayoune',     secteur:'centre',       name:'Clinique Laâyoune',           adresse:'Av. de Mecca, Laâyoune',             telephone:'05 28 89 32 56' },
  { id:71,  ville:'laayoune',     secteur:'hay_amal',     name:'Cabinet Al Massira',          adresse:'Bd Tarfaya, Laâyoune',               telephone:'05 28 89 44 10' },
  { id:72,  ville:'tarfaya',      secteur:'centre',       name:'Cabinet Tarfaya',             adresse:'Av. Hassan II, Tarfaya',             telephone:'05 28 89 76 23' },
  { id:73,  ville:'boujdour',     secteur:'centre',       name:'Cabinet Boujdour',            adresse:'Centre-ville, Boujdour',             telephone:'05 28 89 90 11' },
  // Dakhla-Oued Ed-Dahab
  { id:74,  ville:'dakhla',       secteur:'centre',       name:'Clinique Dakhla',             adresse:'Av. Sidi Ahmed Laroussi, Dakhla',    telephone:'05 28 89 92 44' },
  { id:75,  ville:'dakhla',       secteur:'hay_jadid',    name:'Cabinet Dakhla Médical',      adresse:'Bd Wad Eddahab, Dakhla',             telephone:'05 28 89 95 30' },
]

// ── Secteurs par ville ────────────────────────────────────────
const secteursData = {
  casa:         [ {key:'maarif',name:'Maarif'}, {key:'ain_sebaa',name:'Ain Sebaa'}, {key:'anfa',name:'Anfa'}, {key:'bourgogne',name:'Bourgogne'}, {key:'hay_hassani',name:'Hay Hassani'}, {key:'sidi_maarouf',name:'Sidi Maarouf'}, {key:'derb_sultan',name:'Derb Sultan'}, {key:'ain_chock',name:'Ain Chock'}, {key:'lissasfa',name:'Lissasfa'}, {key:'roches_noires',name:'Roches Noires'}, {key:'sbata',name:'Sbata'}, {key:'belveder',name:'Belvédère'} ],
  mohammedia:   [ {key:'centre',name:'Centre-ville'}, {key:'hay_oulfa',name:'Hay Oulfa'}, {key:'port',name:'Quartier du Port'}, {key:'north',name:'Hay Nord'} ],
  settat:       [ {key:'centre',name:'Centre-ville'}, {key:'hay_salam',name:'Hay Salam'}, {key:'hay_amal',name:'Hay Al Amal'} ],
  berrechid:    [ {key:'centre',name:'Centre-ville'}, {key:'hay_jadid',name:'Hay Jadid'} ],
  benslimane:   [ {key:'centre',name:'Centre-ville'}, {key:'hay_riad',name:'Hay Riad'} ],
  eljadida:     [ {key:'centre',name:'Centre-ville'}, {key:'medina',name:'Médina'}, {key:'hay_jadid',name:'Hay Jadid'}, {key:'azemmour',name:'Azemmour'} ],
  rabat:        [ {key:'agdal',name:'Agdal'}, {key:'hassan',name:'Hassan'}, {key:'souissi',name:'Souissi'}, {key:'hay_riad',name:'Hay Riad'}, {key:'ocean',name:'Océan'}, {key:'akkari',name:'Akkari'}, {key:'youssoufia',name:'Youssoufia'}, {key:'takaddoum',name:'Takaddoum'}, {key:'aviation',name:'Aviation'} ],
  sale:         [ {key:'bab_lamrissa',name:'Bab Lamrissa'}, {key:'hay_salam',name:'Hay Salam'}, {key:'medina',name:'Médina'}, {key:'tabriquet',name:'Tabriquet'}, {key:'hay_karima',name:'Hay Karima'} ],
  temara:       [ {key:'centre',name:'Centre-ville'}, {key:'hay_riad',name:'Hay Riad'}, {key:'menzeh',name:'Menzeh'}, {key:'bir_anzarane',name:'Bir Anzarane'} ],
  kenitra:      [ {key:'centre',name:'Centre-ville'}, {key:'hay_salam',name:'Hay Salam'}, {key:'bir_rami',name:'Bir Rami'}, {key:'port',name:'Quartier du Port'} ],
  khemisset:    [ {key:'centre',name:'Centre-ville'}, {key:'hay_amal',name:'Hay Al Amal'} ],
  sidikacem:    [ {key:'centre',name:'Centre-ville'}, {key:'hay_jadid',name:'Hay Jadid'} ],
  sidi_slimane: [ {key:'centre',name:'Centre-ville'}, {key:'hay_salam',name:'Hay Salam'} ],
  soukarbaa:    [ {key:'centre',name:'Centre-ville'}, {key:'hay_jadid',name:'Hay Jadid'} ],
  fes:          [ {key:'narjiss',name:'Narjiss'}, {key:'ville_nouvelle',name:'Ville Nouvelle'}, {key:'medina',name:'Médina'}, {key:'les_orangers',name:'Les Orangers'}, {key:'agdal',name:'Agdal'}, {key:'dhar_mehraz',name:'Dhar Mehraz'}, {key:'sahrij',name:'Sahrij'} ],
  meknes:       [ {key:'hamria',name:'Hamria'}, {key:'ville_nouvelle',name:'Ville Nouvelle'}, {key:'medina',name:'Médina'}, {key:'bni_mhamed',name:'Bni M\'hamed'}, {key:'ismailia',name:'Hay Ismaïlia'} ],
  taza:         [ {key:'centre',name:'Centre-ville'}, {key:'hay_jadid',name:'Hay Jadid'}, {key:'taza_haute',name:'Taza Haute'} ],
  sefrou:       [ {key:'centre',name:'Centre-ville'}, {key:'medina',name:'Médina'} ],
  ifrane:       [ {key:'centre',name:'Centre-ville'}, {key:'hay_amir',name:'Hay Al Amir'} ],
  azrou:        [ {key:'centre',name:'Centre-ville'}, {key:'hay_amal',name:'Hay Al Amal'} ],
  khenifra:     [ {key:'centre',name:'Centre-ville'}, {key:'hay_salam',name:'Hay Salam'}, {key:'moulay_ismail',name:'Moulay Ismail'} ],
  tanger:       [ {key:'malabata',name:'Malabata'}, {key:'ville_nouvelle',name:'Ville Nouvelle'}, {key:'medina',name:'Médina'}, {key:'beni_makada',name:'Beni Makada'}, {key:'marchane',name:'Marchane'}, {key:'iberia',name:'Iberia'}, {key:'msnana',name:'M\'Snana'} ],
  tetouan:      [ {key:'ensanche',name:'Ensanche'}, {key:'medina',name:'Médina'}, {key:'hay_salam',name:'Hay Salam'}, {key:'centre',name:'Centre-ville'}, {key:'beni_makada',name:'Beni Makada'} ],
  alhoceima:    [ {key:'centre',name:'Centre-ville'}, {key:'ajdir',name:'Ajdir'}, {key:'chabab',name:'Hay Chabab'} ],
  chefchaouen:  [ {key:'medina',name:'Médina'}, {key:'centre',name:'Centre-ville'}, {key:'hay_amal',name:'Hay Al Amal'} ],
  larache:      [ {key:'centre',name:'Centre-ville'}, {key:'medina',name:'Médina'}, {key:'hay_jadid',name:'Hay Jadid'} ],
  mdiq:         [ {key:'plage',name:'Quartier Plage'}, {key:'centre',name:'Centre-ville'} ],
  fnideq:       [ {key:'centre',name:'Centre-ville'}, {key:'port',name:'Quartier du Port'} ],
  ksar_kebir:   [ {key:'centre',name:'Centre-ville'}, {key:'medina',name:'Médina'}, {key:'hay_amal',name:'Hay Al Amal'} ],
  oujda:        [ {key:'hay_qods',name:'Hay Qods'}, {key:'ville_nouvelle',name:'Ville Nouvelle'}, {key:'medina',name:'Médina'}, {key:'sidi_yahia',name:'Sidi Yahia'}, {key:'lazaret',name:'Lazaret'} ],
  nador:        [ {key:'centre',name:'Centre-ville'}, {key:'hay_jadid',name:'Hay Jadid'}, {key:'port',name:'Quartier du Port'} ],
  berkane:      [ {key:'centre',name:'Centre-ville'}, {key:'hay_amal',name:'Hay Al Amal'} ],
  taourirt:     [ {key:'centre',name:'Centre-ville'}, {key:'hay_salam',name:'Hay Salam'} ],
  figuig:       [ {key:'zenaga',name:'Zenaga'}, {key:'centre',name:'Centre-ville'} ],
  driouch:      [ {key:'centre',name:'Centre-ville'}, {key:'hay_jadid',name:'Hay Jadid'} ],
  marrakech:    [ {key:'gueliz',name:'Guéliz'}, {key:'medina',name:'Médina'}, {key:'hivernage',name:'Hivernage'}, {key:'palmeraie',name:'Palmeraie'}, {key:'mhamid',name:'M\'Hamid'}, {key:'targa',name:'Targa'}, {key:'massira',name:'Massira'}, {key:'daoudiate',name:'Daoudiate'} ],
  safi:         [ {key:'centre',name:'Centre-ville'}, {key:'medina',name:'Médina'}, {key:'hay_jadid',name:'Hay Jadid'} ],
  essaouira:    [ {key:'medina',name:'Médina'}, {key:'centre',name:'Centre-ville'}, {key:'hay_salam',name:'Hay Salam'} ],
  benguerir:    [ {key:'centre',name:'Centre-ville'}, {key:'hay_amal',name:'Hay Al Amal'} ],
  youssoufia:   [ {key:'centre',name:'Centre-ville'}, {key:'hay_jadid',name:'Hay Jadid'} ],
  kelaa:        [ {key:'centre',name:'Centre-ville'}, {key:'hay_salam',name:'Hay Salam'} ],
  agadir:       [ {key:'talborjt',name:'Talborjt'}, {key:'hay_mohammadi',name:'Hay Mohammadi'}, {key:'charaf',name:'Charaf'}, {key:'dakhla',name:'Dakhla'}, {key:'anza',name:'Anza'}, {key:'hay_salam',name:'Hay Salam'}, {key:'tikiouine',name:'Tikiouine'} ],
  tiznit:       [ {key:'centre',name:'Centre-ville'}, {key:'medina',name:'Médina'}, {key:'hay_amal',name:'Hay Al Amal'} ],
  taroudant:    [ {key:'centre',name:'Centre-ville'}, {key:'medina',name:'Médina'}, {key:'hay_jadid',name:'Hay Jadid'} ],
  inezgane:     [ {key:'centre',name:'Centre-ville'}, {key:'hay_salam',name:'Hay Salam'}, {key:'bir_anzarane',name:'Bir Anzarane'} ],
  ouldteima:    [ {key:'centre',name:'Centre-ville'}, {key:'hay_jadid',name:'Hay Jadid'} ],
  benmellal:    [ {key:'centre',name:'Centre-ville'}, {key:'hay_ismailia',name:'Hay Ismaïlia'}, {key:'ain_asserdoune',name:'Aïn Asserdoune'}, {key:'hay_amal',name:'Hay Al Amal'} ],
  khouribga:    [ {key:'centre',name:'Centre-ville'}, {key:'ocp',name:'Quartier OCP'}, {key:'hay_jadid',name:'Hay Jadid'} ],
  azilal:       [ {key:'centre',name:'Centre-ville'}, {key:'hay_amal',name:'Hay Al Amal'} ],
  fquih:        [ {key:'centre',name:'Centre-ville'}, {key:'hay_salam',name:'Hay Salam'} ],
  errachidia:   [ {key:'centre',name:'Centre-ville'}, {key:'hay_amal',name:'Hay Al Amal'}, {key:'rissani',name:'Rissani'} ],
  ouarzazate:   [ {key:'centre',name:'Centre-ville'}, {key:'hay_jadid',name:'Hay Jadid'}, {key:'tabount',name:'Tabount'} ],
  zagora:       [ {key:'centre',name:'Centre-ville'}, {key:'hay_amal',name:'Hay Al Amal'} ],
  midelt:       [ {key:'centre',name:'Centre-ville'}, {key:'hay_salam',name:'Hay Salam'} ],
  tinghir:      [ {key:'centre',name:'Centre-ville'}, {key:'hay_jadid',name:'Hay Jadid'} ],
  guelmim:      [ {key:'centre',name:'Centre-ville'}, {key:'hay_amal',name:'Hay Al Amal'}, {key:'hay_salam',name:'Hay Salam'} ],
  tantan:       [ {key:'centre',name:'Centre-ville'}, {key:'hay_jadid',name:'Hay Jadid'} ],
  sidiifni:     [ {key:'centre',name:'Centre-ville'}, {key:'plage',name:'Quartier Plage'} ],
  tata:         [ {key:'centre',name:'Centre-ville'}, {key:'hay_amal',name:'Hay Al Amal'} ],
  laayoune:     [ {key:'centre',name:'Centre-ville'}, {key:'hay_amal',name:'Hay Al Amal'}, {key:'colomina',name:'Colomina'}, {key:'hay_jadid',name:'Hay Jadid'} ],
  tarfaya:      [ {key:'centre',name:'Centre-ville'} ],
  boujdour:     [ {key:'centre',name:'Centre-ville'}, {key:'hay_amal',name:'Hay Al Amal'} ],
  dakhla:       [ {key:'centre',name:'Centre-ville'}, {key:'hay_jadid',name:'Hay Jadid'}, {key:'lagune',name:'Quartier Lagune'} ],
}

function getSecteurs(villeKey) {
  return secteursData[villeKey] || []
}

// ── Couleurs par catégorie médicale ──────────────────────────
const catColors = {
  'Toutes':              { color: '#64748b', bg: '#f1f5f9' },
  'Médecine générale':   { color: '#2563eb', bg: '#eff6ff' },
  'Cardio & Vasculaire': { color: '#dc2626', bg: '#fef2f2' },
  'Neurologie & Psy':    { color: '#7c3aed', bg: '#f5f3ff' },
  'Pédiatrie & Femme':   { color: '#db2777', bg: '#fdf2f8' },
  'Digestif & Rénal':    { color: '#d97706', bg: '#fffbeb' },
  'Os & Articulations':  { color: '#059669', bg: '#f0fdf4' },
  'ORL, Yeux & Peau':    { color: '#0d9488', bg: '#f0fdfa' },
  'Dentaire':            { color: '#0891b2', bg: '#ecfeff' },
  'Endocrine & Métabo':  { color: '#ea580c', bg: '#fff7ed' },
  'Respiratoire':        { color: '#0284c7', bg: '#f0f9ff' },
  'Oncologie':           { color: '#e11d48', bg: '#fff1f2' },
  'Imagerie & Biologie': { color: '#4f46e5', bg: '#eef2ff' },
}
function catColor(cat) { return (catColors[cat] || catColors['Médecine générale']).color }
function catBg(cat)    { return (catColors[cat] || catColors['Médecine générale']).bg }

// ── Recherche et filtre villes ────────────────────────────────
const villeSearch   = ref('')
const regionFilter  = ref('Toutes')

const regions = computed(() => {
  const set = new Set(villes.map(v => v.region))
  return ['Toutes', ...set]
})

const filteredVilles = computed(() => {
  return villes.filter(v => {
    const matchRegion = regionFilter.value === 'Toutes' || v.region === regionFilter.value
    const matchSearch = !villeSearch.value || v.name.toLowerCase().includes(villeSearch.value.toLowerCase())
    return matchRegion && matchSearch
  })
})

function getCabinets(villeKey, secteurKey) {
  return cabinetsData.filter(c => c.ville === villeKey && (!secteurKey || c.secteur === secteurKey))
}

// ── Pool de médecins (généré dynamiquement par cabinet) ──────
const _medecinPool = [
  { name:'Karimi Hassan',   spec:'Médecine générale',            color:'#2563eb', initials:'KH', experience:14, rating:4.8, tarif:200 },
  { name:'Saidi Nour',      spec:'Orthodontie',                  color:'#7c3aed', initials:'SN', experience:9,  rating:4.5, tarif:250 },
  { name:'Tahiri Rachid',   spec:'Chirurgie dentaire',           color:'#059669', initials:'TR', experience:17, rating:4.9, tarif:180 },
  { name:'Bennis Samia',    spec:'Cardiologie',                  color:'#be185d', initials:'BS', experience:11, rating:4.6, tarif:220 },
  { name:'Oujdi Amine',     spec:'Pédiatrie',                    color:'#b45309', initials:'OA', experience:8,  rating:4.3, tarif:160 },
  { name:'Idrissi Mehdi',   spec:'Neurologie',                   color:'#0891b2', initials:'IM', experience:20, rating:4.9, tarif:350 },
  { name:'Cherkaoui Leila', spec:'Gynécologie',                  color:'#c026d3', initials:'CL', experience:15, rating:4.7, tarif:280 },
  { name:'Alaoui Samir',    spec:'Médecine interne',             color:'#16a34a', initials:'AS', experience:22, rating:4.8, tarif:300 },
  { name:'Benali Samira',   spec:'ORL',                          color:'#7c3aed', initials:'BS', experience:13, rating:4.7, tarif:230 },
  { name:'Hajji Laila',     spec:'Ophtalmologie',                color:'#c2410c', initials:'HL', experience:8,  rating:4.4, tarif:200 },
  { name:'Alami Fatima',    spec:'Dermatologie',                 color:'#be185d', initials:'AF', experience:16, rating:4.8, tarif:180 },
  { name:'Ziani Youssef',   spec:'Pneumologie',                  color:'#2563eb', initials:'ZY', experience:10, rating:4.5, tarif:210 },
  { name:'Ouali Nadia',     spec:'Rhumatologie',                 color:'#6d28d9', initials:'ON', experience:18, rating:4.9, tarif:320 },
  { name:'Tahiri Nadia',    spec:'Endocrinologie',               color:'#b45309', initials:'TN', experience:12, rating:4.6, tarif:280 },
  { name:'El Fassi Bilal',  spec:'Gastro-entérologie',           color:'#0f766e', initials:'FB', experience:9,  rating:4.4, tarif:220 },
  { name:'Moussaid Sara',   spec:'Psychiatrie',                  color:'#9333ea', initials:'MS', experience:11, rating:4.5, tarif:260 },
  { name:'Benjelloun Omar', spec:'Chirurgie orthopédique',       color:'#dc2626', initials:'BO', experience:19, rating:4.8, tarif:400 },
  { name:'Chraibi Amina',   spec:'Radiologie',                   color:'#0284c7', initials:'CA', experience:7,  rating:4.3, tarif:300 },
  { name:'Filali Driss',    spec:'Urologie',                     color:'#15803d', initials:'FD', experience:14, rating:4.7, tarif:280 },
  { name:'Hajjami Kenza',   spec:'Oncologie',                    color:'#e11d48', initials:'HK', experience:16, rating:4.9, tarif:450 },
  { name:'Lahlou Yassine',  spec:'Cardiologie interventionnelle',color:'#0891b2', initials:'LY', experience:21, rating:5.0, tarif:500 },
  { name:'Mansouri Hind',   spec:'Néonatologie',                 color:'#d97706', initials:'MH', experience:13, rating:4.6, tarif:200 },
  { name:'Naciri Adil',     spec:'Chirurgie vasculaire',         color:'#7c3aed', initials:'NA', experience:15, rating:4.7, tarif:380 },
  { name:'Qasmi Leila',     spec:'Immunologie',                  color:'#be185d', initials:'QL', experience:10, rating:4.5, tarif:250 },
]

function getMedecins(cabinetId) {
  if (!cabinetId) return []
  const seed = cabinetId * 7
  const i1 = seed % _medecinPool.length
  const i2 = (seed + 3) % _medecinPool.length
  const i3 = (seed + 11) % _medecinPool.length
  const picks = [i1, i2, i3].filter((v,i,a) => a.indexOf(v) === i)
  return picks.map((idx, i) => ({
    id: cabinetId * 100 + i,
    cabinet: cabinetId,
    ..._medecinPool[idx],
  }))
}

// ── Date / créneaux ──────────────────────────────────────────
const slotPeriods = [
  { key: 'morning',   label: 'Matin',      emoji: '🌅' },
  { key: 'afternoon', label: 'Après-midi', emoji: '☀️' },
  { key: 'evening',   label: 'Soir',       emoji: '🌇' },
]
const allSlots = {
  morning:   ['08:00','08:30','09:00','09:30','10:00','10:30','11:00','11:30'],
  afternoon: ['12:00','13:00','14:00','14:30','15:00','15:30','16:00'],
  evening:   ['16:30','17:00','17:30','18:00','18:30'],
}

function getSlots(period) {
  const seed = (rdv.value.medecin?.id || 1) + (rdv.value.form.date?.charCodeAt(0) || 0)
  return allSlots[period].map((time, i) => ({ time, taken: (seed + i * 3) % 7 === 0 }))
}

const bookingDates = computed(() => {
  const days   = ['Dim','Lun','Mar','Mer','Jeu','Ven','Sam']
  const months = ['Jan','Fév','Mar','Avr','Mai','Jun','Jul','Aoû','Sep','Oct','Nov','Déc']
  const now = new Date()
  return Array.from({ length: 7 }, (_, i) => {
    const d = new Date(now); d.setDate(now.getDate() + i)
    return { key: `${d.getDate()} ${months[d.getMonth()]}`, dayName: days[d.getDay()], dayNum: d.getDate(), month: months[d.getMonth()] }
  })
})
</script>

<style scoped>
/* ══ ROOT ══ */
.dash-root { display:flex; min-height:100vh; background:#f1f5f9; font-family:inherit; }

/* ══ SIDEBAR ══ */
.sidebar {
  width:256px; flex-shrink:0;
  background:#0f172a;
  display:flex; flex-direction:column;
  position:fixed; top:0; left:0; height:100vh;
  z-index:100; transition:transform .3s;
}
@media(max-width:768px){ .sidebar{ transform:translateX(-100%); } .sidebar.sidebar-open{ transform:translateX(0); } }
.sidebar-overlay { display:none; position:fixed; inset:0; background:rgba(0,0,0,.6); z-index:99; backdrop-filter:blur(3px); }
@media(max-width:768px){ .sidebar-overlay{ display:block; } }

/* Brand */
.sb-brand { display:flex; align-items:center; gap:12px; padding:24px 20px 16px; }
.sb-logo { width:38px; height:38px; background:linear-gradient(135deg,#2563eb,#0ea5e9); border-radius:10px; display:flex; align-items:center; justify-content:center; flex-shrink:0; }
.sb-appname { font-size:16px; font-weight:800; color:#fff; margin:0; letter-spacing:-.01em; }
.sb-tagline { font-size:10px; color:rgba(255,255,255,.35); margin:1px 0 0; font-weight:500; text-transform:uppercase; letter-spacing:.08em; }

/* Section label */
.sb-section-lbl { font-size:10px; font-weight:700; color:rgba(255,255,255,.2); letter-spacing:.12em; padding:14px 20px 6px; text-transform:uppercase; }

/* Nav */
.sb-nav { padding:0 10px; display:flex; flex-direction:column; gap:2px; }
.sb-item { display:flex; align-items:center; gap:11px; padding:10px 12px; border-radius:10px; border:none; cursor:pointer; background:transparent; font-size:13px; font-weight:500; color:rgba(255,255,255,.45); text-align:left; transition:all .18s; width:100%; position:relative; overflow:hidden; }
.sb-item::before { content:''; position:absolute; left:0; top:50%; transform:translateY(-50%); width:3px; height:0; background:#3b82f6; border-radius:0 3px 3px 0; transition:height .18s; }
.sb-item:hover { background:rgba(255,255,255,.06); color:rgba(255,255,255,.85); }
.sb-item.active { background:rgba(59,130,246,.18); color:#fff; font-weight:600; }
.sb-item.active::before { height:22px; }
.sb-item-icon { display:flex; flex-shrink:0; }
.sb-item-lbl { flex:1; }
.sb-count { background:#2563eb; color:#fff; font-size:10px; font-weight:800; padding:1px 7px; border-radius:99px; }

/* User card */
.sb-usercard { margin:8px 10px 16px; padding:12px 14px; background:rgba(255,255,255,.05); border:1px solid rgba(255,255,255,.08); border-radius:12px; display:flex; align-items:center; gap:10px; }
.sb-useravatar { width:36px; height:36px; border-radius:10px; background:linear-gradient(135deg,#2563eb,#7c3aed); display:flex; align-items:center; justify-content:center; color:#fff; font-weight:800; font-size:14px; flex-shrink:0; }
.sb-userinfo { flex:1; min-width:0; }
.sb-username { font-size:13px; font-weight:700; color:#fff; margin:0; white-space:nowrap; overflow:hidden; text-overflow:ellipsis; }
.sb-online { display:flex; align-items:center; gap:5px; margin-top:2px; }
.sb-online-dot { width:6px; height:6px; border-radius:50%; background:#22c55e; animation:pulse-dot 2s infinite; flex-shrink:0; }
@keyframes pulse-dot { 0%,100%{ box-shadow:0 0 0 0 rgba(34,197,94,.5); } 50%{ box-shadow:0 0 0 4px rgba(34,197,94,0); } }
.sb-online span { font-size:10px; color:rgba(255,255,255,.4); font-weight:500; }

/* ══ MAIN ══ */
.main-content { flex:1; margin-left:256px; display:flex; flex-direction:column; min-height:100vh; }
@media(max-width:768px){ .main-content{ margin-left:0; } }

/* ══ TOPBAR ══ */
.topbar { height:66px; background:#fff; border-bottom:1px solid #e8ecf0; display:flex; align-items:center; padding:0 28px; gap:16px; position:sticky; top:0; z-index:50; box-shadow:0 1px 8px rgba(0,0,0,.04); }
.tb-hamburger { display:none; background:none; border:none; cursor:pointer; color:#64748b; padding:4px; }
@media(max-width:768px){ .tb-hamburger{ display:flex; } }
.tb-breadcrumb { display:flex; align-items:center; gap:8px; }
.tb-app { font-size:12px; color:#94a3b8; font-weight:500; }
.tb-page { font-size:15px; font-weight:700; color:#0f172a; }
.tb-right { margin-left:auto; display:flex; align-items:center; gap:10px; }
.tb-date-chip { font-size:12px; color:#64748b; background:#f8fafc; border:1px solid #e8ecf0; padding:5px 12px; border-radius:8px; font-weight:500; }
@media(max-width:900px){ .tb-date-chip{ display:none; } }
.tb-notif { position:relative; cursor:pointer; padding:7px; border-radius:10px; color:#64748b; transition:background .18s; border:none; background:none; display:flex; }
.tb-notif:hover { background:#f1f5f9; }
.tb-notif-badge { position:absolute; top:3px; right:3px; width:15px; height:15px; background:#ef4444; color:#fff; border-radius:50%; font-size:9px; font-weight:800; display:flex; align-items:center; justify-content:center; border:2px solid #fff; }
.user-avatar-top { width:36px; height:36px; background:linear-gradient(135deg,#2563eb,#7c3aed); border-radius:10px; display:flex; align-items:center; justify-content:center; color:#fff; font-weight:800; font-size:13px; cursor:pointer; }

/* ══ PAGE ══ */
.page-content { padding:28px; max-width:1080px; display:flex; flex-direction:column; gap:22px; }

/* ══ HERO ══ */
.hero-card { background:linear-gradient(135deg,#1d4ed8 0%,#2563eb 45%,#0ea5e9 100%); border-radius:22px; padding:36px 40px; display:flex; align-items:center; gap:24px; overflow:hidden; position:relative; }
.hero-card::before { content:''; position:absolute; top:-50px; right:240px; width:220px; height:220px; border-radius:50%; background:rgba(255,255,255,.05); }
.hero-card::after  { content:''; position:absolute; bottom:-60px; left:300px; width:280px; height:280px; border-radius:50%; background:rgba(255,255,255,.04); }
.hero-left { flex:1; min-width:0; position:relative; z-index:1; }
.hero-eyebrow { font-size:11px; font-weight:700; color:rgba(255,255,255,.6); text-transform:uppercase; letter-spacing:.1em; margin:0 0 8px; }
.hero-title { font-size:26px; font-weight:800; color:#fff; margin:0 0 10px; line-height:1.25; }
.hero-sub { font-size:14px; color:rgba(255,255,255,.75); margin:0 0 24px; max-width:360px; line-height:1.6; }
.hero-cta { display:inline-flex; align-items:center; gap:8px; padding:12px 22px; background:#fff; color:#1d4ed8; border:none; border-radius:12px; font-size:14px; font-weight:700; cursor:pointer; transition:all .2s; box-shadow:0 4px 20px rgba(0,0,0,.18); }
.hero-cta:hover { transform:translateY(-2px); box-shadow:0 8px 28px rgba(0,0,0,.22); }
.hero-right { flex-shrink:0; position:relative; z-index:1; }
@media(max-width:768px){ .hero-right{ display:none; } }

/* Floating appointment card */
.hero-float-card { background:rgba(255,255,255,.12); backdrop-filter:blur(12px); border:1px solid rgba(255,255,255,.2); border-radius:18px; padding:18px 20px; min-width:230px; }
.hfc-top { display:flex; align-items:center; gap:10px; margin-bottom:12px; }
.hfc-avatar { width:38px; height:38px; border-radius:10px; display:flex; align-items:center; justify-content:center; color:#fff; font-weight:800; font-size:15px; flex-shrink:0; }
.hfc-name { font-size:13px; font-weight:700; color:#fff; margin:0 0 1px; }
.hfc-spec { font-size:11px; color:rgba(255,255,255,.6); margin:0; }
.hfc-badge { background:rgba(34,197,94,.25); color:#86efac; font-size:10px; font-weight:700; padding:3px 9px; border-radius:99px; white-space:nowrap; flex-shrink:0; }
.hfc-divider { height:1px; background:rgba(255,255,255,.12); margin-bottom:12px; }
.hfc-bottom { display:flex; justify-content:space-between; align-items:center; }
.hfc-date { font-size:11px; color:rgba(255,255,255,.6); }
.hfc-time { font-size:15px; font-weight:800; color:#fff; }

/* ══ STATS ══ */
.stats-row { display:grid; grid-template-columns:repeat(4,1fr); gap:16px; }
@media(max-width:900px){ .stats-row{ grid-template-columns:repeat(2,1fr); } }
@media(max-width:500px){ .stats-row{ grid-template-columns:1fr; } }
.stat-tile { background:#fff; border-radius:16px; padding:20px; display:flex; align-items:center; gap:16px; box-shadow:0 1px 3px rgba(0,0,0,.05),0 4px 12px rgba(0,0,0,.03); border:1px solid #f1f5f9; transition:transform .18s,box-shadow .18s; }
.stat-tile:hover { transform:translateY(-2px); box-shadow:0 4px 20px rgba(0,0,0,.08); }
.stat-tile-icon { width:46px; height:46px; border-radius:12px; display:flex; align-items:center; justify-content:center; flex-shrink:0; }
.stat-tile-val { font-size:20px; font-weight:800; color:#0f172a; margin:0; }
.stat-tile-lbl { font-size:12px; color:#94a3b8; margin:2px 0 0; font-weight:500; }

/* ══ HOME GRID ══ */
.home-2col { display:grid; grid-template-columns:1fr 1fr; gap:20px; }
@media(max-width:900px){ .home-2col{ grid-template-columns:1fr; } }

/* ══ PANEL ══ */
.panel { background:#fff; border-radius:18px; padding:22px; box-shadow:0 1px 3px rgba(0,0,0,.05),0 4px 12px rgba(0,0,0,.03); border:1px solid #f1f5f9; }
.panel-header { display:flex; align-items:center; justify-content:space-between; margin-bottom:18px; }
.panel-header h4 { font-size:15px; font-weight:700; color:#0f172a; margin:0; }
.pill-green { background:#dcfce7; color:#16a34a; font-size:11px; font-weight:700; padding:3px 11px; border-radius:99px; }
.link-btn { background:none; border:none; color:#2563eb; font-size:13px; font-weight:600; cursor:pointer; padding:0; }

/* Prochain RDV */
.appt-card { display:flex; gap:16px; }
.appt-date-block { text-align:center; flex-shrink:0; background:#eff6ff; border-radius:14px; padding:14px 18px; }
.appt-day { font-size:28px; font-weight:800; color:#1d4ed8; margin:0; line-height:1; }
.appt-month { font-size:10px; font-weight:700; color:#93c5fd; margin:2px 0 0; text-transform:uppercase; letter-spacing:.06em; }
.appt-vline { width:1px; background:#f1f5f9; flex-shrink:0; }
.appt-body { flex:1; min-width:0; }
.appt-doc-row { display:flex; align-items:center; gap:10px; margin-bottom:12px; }
.appt-doc-avatar { width:40px; height:40px; border-radius:10px; display:flex; align-items:center; justify-content:center; color:#fff; font-weight:800; font-size:15px; flex-shrink:0; }
.appt-doc-name { font-size:14px; font-weight:700; color:#0f172a; margin:0 0 3px; }
.appt-spec-pill { background:#f5f3ff; color:#7c3aed; font-size:11px; font-weight:600; padding:2px 9px; border-radius:99px; }
.appt-meta-line { display:flex; align-items:center; gap:6px; font-size:12px; color:#64748b; margin:0 0 6px; }
.appt-btn-cancel { padding:7px 14px; border:1.5px solid #fecaca; color:#dc2626; background:#fff; border-radius:9px; font-size:12px; font-weight:600; cursor:pointer; transition:all .18s; }
.appt-btn-cancel:hover { background:#fef2f2; }

/* Quick actions */
.quick-list { display:flex; flex-direction:column; gap:8px; }
.quick-item { display:flex; align-items:center; gap:14px; padding:13px 14px; border-radius:13px; border:1.5px solid #f1f5f9; background:#fff; cursor:pointer; transition:all .18s; text-align:left; }
.quick-item:hover { border-color:#dbeafe; background:#fafbff; transform:translateX(3px); }
.qi-icon { width:44px; height:44px; border-radius:11px; display:flex; align-items:center; justify-content:center; flex-shrink:0; }
.qi-title { font-size:13px; font-weight:700; color:#0f172a; margin:0 0 2px; }
.qi-sub { font-size:11px; color:#94a3b8; margin:0; }

/* Historique */
.hist-list { display:flex; flex-direction:column; }
.hist-row { display:flex; align-items:center; gap:14px; padding:12px 0; border-bottom:1px solid #f8fafc; }
.hist-row:last-child { border-bottom:none; }
.hist-avatar { width:40px; height:40px; border-radius:10px; display:flex; align-items:center; justify-content:center; color:#fff; font-weight:700; font-size:14px; flex-shrink:0; }
.hist-info { flex:1; }
.hist-doc { font-size:13px; font-weight:600; color:#0f172a; margin:0 0 2px; }
.hist-meta { font-size:12px; color:#94a3b8; margin:0; }
.hist-badge { font-size:11px; font-weight:700; padding:4px 11px; border-radius:99px; white-space:nowrap; }
.hbadge-done { background:#dcfce7; color:#16a34a; }
.hbadge-planned { background:#eff6ff; color:#2563eb; }
.hbadge-cancelled { background:#fef2f2; color:#dc2626; }

/* ══ MES RDV ══ */
.rdv-full-card { background:#fff; border-radius:16px; overflow:hidden; box-shadow:0 1px 3px rgba(0,0,0,.05),0 4px 12px rgba(0,0,0,.03); border:1px solid #f1f5f9; }
.rfc-status-bar { height:4px; }
.rfc-planned { background:linear-gradient(90deg,#2563eb,#0ea5e9); }
.rfc-done { background:linear-gradient(90deg,#16a34a,#22c55e); }
.rfc-cancelled { background:linear-gradient(90deg,#dc2626,#f87171); }
.rfc-body { display:flex; align-items:flex-start; gap:16px; padding:20px; }
.rfc-avatar { width:46px; height:46px; border-radius:12px; display:flex; align-items:center; justify-content:center; color:#fff; font-weight:800; font-size:16px; flex-shrink:0; }
.rfc-info { flex:1; }
.rfc-doctor { font-size:15px; font-weight:700; color:#0f172a; margin:0 0 2px; }
.rfc-spec { font-size:12px; color:#64748b; margin:0 0 10px; }
.rfc-meta-row { display:flex; flex-wrap:wrap; gap:8px; }
.rfc-meta-chip { display:flex; align-items:center; gap:5px; font-size:12px; color:#64748b; background:#f8fafc; padding:4px 10px; border-radius:8px; }
.rfc-right { display:flex; flex-direction:column; align-items:flex-end; gap:8px; flex-shrink:0; }
.rdv-badge { font-size:11px; font-weight:700; padding:4px 11px; border-radius:99px; }
.badge-planned { background:#eff6ff; color:#2563eb; }
.badge-done { background:#dcfce7; color:#16a34a; }
.badge-cancelled { background:#fef2f2; color:#dc2626; }
.btn-cancel { font-size:12px; color:#dc2626; background:#fef2f2; border:none; border-radius:8px; padding:5px 12px; cursor:pointer; font-weight:600; }

.appt-empty { display:flex; flex-direction:column; align-items:center; gap:10px; padding:32px 16px; text-align:center; color:#94a3b8; }
.appt-empty p { font-size:13px; color:#94a3b8; margin:0; }
.hist-empty { padding:24px 16px; text-align:center; }
.hist-empty p { font-size:13px; color:#94a3b8; margin:0; }
.rdv-empty { display:flex; flex-direction:column; align-items:center; gap:14px; padding:80px 24px; text-align:center; }
.rdv-empty-icon { width:80px; height:80px; background:#f1f5f9; border-radius:24px; display:flex; align-items:center; justify-content:center; }
.rdv-empty h3 { font-size:18px; font-weight:700; color:#0f172a; margin:0; }
.rdv-empty p  { font-size:14px; color:#64748b; margin:0; max-width:320px; line-height:1.6; }
.btn-submit-sm { padding:12px 28px; background:linear-gradient(135deg,#2563eb,#1d4ed8); color:#fff; border:none; border-radius:12px; font-size:14px; font-weight:700; cursor:pointer; box-shadow:0 4px 14px rgba(37,99,235,.3); transition:all .2s; }
.btn-submit-sm:hover { transform:translateY(-1px); box-shadow:0 6px 20px rgba(37,99,235,.4); }

/* ══ DOSSIER ══ */
.dossier-hero { background:linear-gradient(135deg,#eff6ff,#e0e7ff); border-radius:18px; padding:24px 28px; display:flex; align-items:center; gap:18px; border:1px solid #dbeafe; }
.dossier-icon { width:64px; height:64px; background:#fff; border-radius:16px; display:flex; align-items:center; justify-content:center; flex-shrink:0; box-shadow:0 4px 16px rgba(37,99,235,.12); }
.dossier-hero h3 { font-size:17px; font-weight:800; color:#1e40af; margin:0 0 3px; }
.dossier-hero p { font-size:12px; color:#3b82f6; margin:0; }
.dossier-hero .pill-green { margin-left:auto; flex-shrink:0; }

/* ── Wizard ── */
.wizard-steps { display:flex; align-items:flex-start; background:#fff; border-radius:16px; padding:16px 20px; margin-bottom:24px; box-shadow:0 1px 3px rgba(0,0,0,.06); overflow-x:auto; gap:0; }
.wizard-step { display:flex; flex-direction:column; align-items:center; gap:6px; position:relative; flex:1; min-width:70px; }
.ws-icon { width:36px; height:36px; border-radius:50%; background:#e2e8f0; display:flex; align-items:center; justify-content:center; color:#94a3b8; transition:all .3s; }
.wizard-step.active .ws-icon { background:#2563eb; color:#fff; box-shadow:0 0 0 4px rgba(37,99,235,.15); }
.wizard-step.done   .ws-icon { background:#22c55e; color:#fff; }
.ws-label { font-size:11px; font-weight:600; color:#94a3b8; white-space:nowrap; }
.wizard-step.active .ws-label { color:#2563eb; }
.wizard-step.done   .ws-label { color:#22c55e; }
.ws-line { position:absolute; top:18px; left:calc(50% + 18px); right:calc(-50% + 18px); height:2px; background:#e2e8f0; transition:background .3s; }
.ws-line.filled { background:#22c55e; }

.step-body { animation: fadeIn .25s ease; }
@keyframes fadeIn { from{ opacity:0; transform:translateY(8px); } to{ opacity:1; transform:none; } }

.step-hint { font-size:15px; font-weight:600; color:#374151; margin:0 0 16px; }
.step-recap { display:flex; align-items:center; gap:6px; flex-wrap:wrap; margin-bottom:16px; }
.recap-pill { background:#eff6ff; color:#1d4ed8; font-size:12px; font-weight:600; padding:4px 12px; border-radius:20px; }
.recap-sep { color:#94a3b8; font-size:14px; }

/* ══ Step 1 : Domaines ══ */
.d1-hero {
  display:flex; align-items:center; justify-content:space-between;
  background:linear-gradient(135deg,#0f172a 0%,#1e3a8a 55%,#1d4ed8 100%);
  border-radius:20px; padding:28px 28px; margin-bottom:24px;
  position:relative; overflow:hidden;
}
.d1-hero::before {
  content:''; position:absolute; top:-60px; right:-60px;
  width:220px; height:220px; border-radius:50%; background:rgba(255,255,255,.05);
}
.d1-hero-tag {
  display:inline-block; background:rgba(255,255,255,.12); color:rgba(255,255,255,.9);
  font-size:11px; font-weight:700; padding:4px 12px; border-radius:99px;
  letter-spacing:.06em; margin-bottom:12px;
}
.d1-hero-title { font-size:22px; font-weight:800; color:#fff; margin:0 0 8px; line-height:1.25; }
.d1-hero-title span { color:#93c5fd; }
.d1-hero-sub { font-size:13px; color:rgba(255,255,255,.55); margin:0; }
.d1-hero-right { display:flex; align-items:center; gap:0; background:rgba(255,255,255,.08); border-radius:16px; padding:16px 22px; border:1px solid rgba(255,255,255,.12); flex-shrink:0; }
.d1-stat { text-align:center; padding:0 16px; }
.d1-stat-num { font-size:26px; font-weight:900; color:#fff; margin:0 0 2px; line-height:1; }
.d1-stat-lbl { font-size:10px; color:rgba(255,255,255,.5); margin:0; font-weight:600; }
.d1-stat-sep { width:1px; height:36px; background:rgba(255,255,255,.15); }

.d1-search-wrap {
  display:flex; align-items:center; gap:12px;
  background:#fff; border:2px solid #e2e8f0; border-radius:14px;
  padding:0 16px; margin-bottom:18px;
  transition:border-color .2s, box-shadow .2s;
}
.d1-search-wrap:focus-within { border-color:#2563eb; box-shadow:0 0 0 4px rgba(37,99,235,.1); }
.d1-search-input { flex:1; border:none; outline:none; padding:14px 0; font-size:15px; color:#0f172a; background:transparent; }
.d1-search-input::placeholder { color:#94a3b8; }
.d1-search-clear { background:none; border:none; cursor:pointer; color:#94a3b8; font-size:13px; font-weight:700; padding:0; }

.d1-cats { display:flex; gap:8px; flex-wrap:wrap; margin-bottom:22px; }
.d1-cat {
  padding:7px 16px; border-radius:99px; font-size:12px; font-weight:600;
  border:1.5px solid; background:#fff; cursor:pointer; transition:all .2s;
  white-space:nowrap;
}
.d1-cat:hover { transform:translateY(-1px); filter:brightness(.96); }

/* Grille portrait */
.d1-grid { display:grid; grid-template-columns:repeat(auto-fill,minmax(160px,1fr)); gap:14px; }

.d1-card {
  display:flex; flex-direction:column;
  background:#fff; border:2px solid #f1f5f9; border-radius:18px;
  cursor:pointer; transition:all .22s cubic-bezier(.4,0,.2,1);
  text-align:center; overflow:hidden; padding:0;
}
.d1-card:hover { transform:translateY(-4px); box-shadow:0 12px 32px rgba(0,0,0,.1); border-color:#e2e8f0; }
.d1-card.selected { transform:translateY(-4px); }

.d1-card-top {
  padding:22px 16px 14px;
  display:flex; align-items:center; justify-content:center;
}
.d1-card-emoji-wrap {
  width:60px; height:60px; border-radius:18px;
  display:flex; align-items:center; justify-content:center;
  transition:transform .22s;
}
.d1-card:hover .d1-card-emoji-wrap { transform:scale(1.08); }
.d1-card-emoji { font-size:28px; line-height:1; }

.d1-card-body { padding:0 14px 12px; }
.d1-card-name {
  font-size:13px; font-weight:700; color:#0f172a; margin:0 0 8px;
  line-height:1.3; min-height:32px;
  display:-webkit-box; -webkit-line-clamp:2; -webkit-box-orient:vertical; overflow:hidden;
}
.d1-card-badge {
  font-size:10px; font-weight:700; padding:3px 10px; border-radius:99px;
  display:inline-block; letter-spacing:.02em;
}

.d1-card-footer {
  padding:8px 14px; margin-top:auto;
  border-top:1px solid rgba(0,0,0,.04);
  display:flex; align-items:center; justify-content:center;
}

.empty-state { display:flex; flex-direction:column; align-items:center; gap:10px; padding:56px 24px; color:#94a3b8; text-align:center; }
.empty-state p { font-size:14px; color:#64748b; margin:0; }
.empty-state strong { color:#374151; }
.btn-link { background:none; border:none; color:#2563eb; font-size:13px; font-weight:600; cursor:pointer; padding:0; text-decoration:underline; }

/* ── Step 2: Villes ── */
.villes-grid { display:grid; grid-template-columns:repeat(auto-fill,minmax(160px,1fr)); gap:12px; }
.ville-card { display:flex; flex-direction:column; align-items:center; gap:8px; padding:18px 12px; background:#fff; border:2px solid #e2e8f0; border-radius:16px; cursor:pointer; transition:all .2s; text-align:center; }
.ville-card:hover { border-color:#93c5fd; transform:translateY(-2px); box-shadow:0 8px 20px rgba(37,99,235,.1); }
.ville-card.selected { border-color:#2563eb; background:#eff6ff; }
.ville-flag { font-size:28px; }
.ville-name { font-size:14px; font-weight:700; color:#0f172a; }
.ville-card.selected .ville-name { color:#1d4ed8; }
.ville-count { font-size:11px; color:#94a3b8; font-weight:500; }

/* ── Step 3: Secteurs ── */
.secteur-hero { display:flex; align-items:center; gap:16px; background:linear-gradient(135deg,#f0fdf4 0%,#dcfce7 100%); border-radius:18px; padding:20px 22px; margin-bottom:20px; border:1px solid #bbf7d0; }
.secteur-hero-icon { font-size:28px; width:56px; height:56px; background:#fff; border-radius:14px; display:flex; align-items:center; justify-content:center; box-shadow:0 4px 14px rgba(22,163,74,.12); flex-shrink:0; }
.secteur-hero-text { flex:1; }
.secteur-hero-text h3 { font-size:16px; font-weight:700; color:#14532d; margin:0 0 3px; }
.secteur-hero-text p  { font-size:12px; color:#16a34a; margin:0; }
.secteur-hero-badge { display:flex; flex-direction:column; align-items:center; background:#16a34a; color:#fff; border-radius:12px; padding:8px 14px; flex-shrink:0; }
.secteur-hero-badge span { font-size:22px; font-weight:800; line-height:1; }
.secteur-hero-badge small { font-size:10px; font-weight:600; opacity:.85; }

.secteurs-grid { display:grid; grid-template-columns:repeat(auto-fill,minmax(230px,1fr)); gap:11px; }
.secteur-card { display:flex; align-items:center; gap:14px; padding:15px 16px; background:#fff; border:1.5px solid #e2e8f0; border-radius:14px; cursor:pointer; transition:all .18s; text-align:left; width:100%; }
.secteur-card:hover:not(.unavailable) { border-color:#86efac; box-shadow:0 6px 20px rgba(22,163,74,.1); transform:translateY(-2px); }
.secteur-card.selected { border-color:#16a34a; background:#f0fdf4; box-shadow:0 0 0 3px rgba(22,163,74,.12); }
.secteur-card.unavailable { opacity:.4; cursor:not-allowed; filter:grayscale(.4); }

.secteur-icon-wrap { width:44px; height:44px; border-radius:12px; background:#f0fdf4; color:#16a34a; display:flex; align-items:center; justify-content:center; flex-shrink:0; transition:background .18s; }
.secteur-card:hover:not(.unavailable) .secteur-icon-wrap { background:#dcfce7; }
.secteur-card.selected .secteur-icon-wrap { background:#dcfce7; color:#15803d; }
.secteur-body { flex:1; min-width:0; }
.secteur-name { font-size:14px; font-weight:700; color:#0f172a; margin:0 0 3px; white-space:nowrap; overflow:hidden; text-overflow:ellipsis; }
.secteur-avail { font-size:11px; color:#16a34a; font-weight:600; margin:0; }
.secteur-none  { font-size:11px; color:#cbd5e1; margin:0; }
.secteur-chevron { color:#cbd5e1; flex-shrink:0; transition:color .18s; }
.secteur-card:hover:not(.unavailable) .secteur-chevron { color:#16a34a; }
.secteur-card.selected .secteur-chevron { color:#16a34a; }

/* ── Step 4: Cabinets ── */
.cabinets-list { display:flex; flex-direction:column; gap:12px; }
.cabinet-card { display:flex; align-items:center; gap:16px; padding:16px 20px; background:#fff; border:2px solid #e2e8f0; border-radius:16px; cursor:pointer; transition:all .2s; text-align:left; }
.cabinet-card:hover { border-color:#93c5fd; box-shadow:0 4px 16px rgba(37,99,235,.08); }
.cabinet-card.selected { border-color:#2563eb; background:#eff6ff; }
.cabinet-icon { width:48px; height:48px; background:#eff6ff; border-radius:14px; display:flex; align-items:center; justify-content:center; color:#2563eb; flex-shrink:0; }
.cabinet-card.selected .cabinet-icon { background:#dbeafe; }
.cabinet-info { flex:1; }
.cabinet-info h5 { font-size:15px; font-weight:700; color:#0f172a; margin:0 0 4px; }
.cabinet-info p { display:flex; align-items:center; gap:5px; font-size:12px; color:#64748b; margin:2px 0; }
.cabinet-arrow { color:#94a3b8; }
.cabinet-card:hover .cabinet-arrow, .cabinet-card.selected .cabinet-arrow { color:#2563eb; }

/* ── Step 4: Médecins ── */
.medecins-list { display:flex; flex-direction:column; gap:12px; }
.medecin-card { display:flex; align-items:center; gap:16px; padding:16px 20px; background:#fff; border:2px solid #e2e8f0; border-radius:16px; cursor:pointer; transition:all .2s; text-align:left; position:relative; }
.medecin-card:hover { border-color:#93c5fd; box-shadow:0 4px 16px rgba(37,99,235,.08); }
.medecin-card.selected { border-color:#2563eb; background:#eff6ff; }
.medecin-avatar { width:52px; height:52px; border-radius:50%; display:flex; align-items:center; justify-content:center; color:#fff; font-weight:700; font-size:18px; flex-shrink:0; }
.medecin-info { flex:1; }
.medecin-info h5 { font-size:15px; font-weight:700; color:#0f172a; margin:0 0 3px; }
.medecin-spec { font-size:12px; color:#64748b; margin:0 0 6px; }
.medecin-meta { display:flex; align-items:center; gap:12px; flex-wrap:wrap; }
.medecin-meta span { display:flex; align-items:center; gap:4px; font-size:12px; color:#64748b; }
.tarif-badge { background:#eff6ff; color:#1d4ed8; font-weight:700; padding:2px 10px; border-radius:20px; font-size:12px; }
.check-mark { width:28px; height:28px; background:#2563eb; border-radius:50%; display:flex; align-items:center; justify-content:center; flex-shrink:0; }

/* ── Step 5: Confirmer ── */
.confirm-grid { display:grid; grid-template-columns:1fr 1fr; gap:20px; }
@media(max-width:700px){ .confirm-grid{ grid-template-columns:1fr; } }
.confirm-section { background:#fff; border-radius:16px; padding:20px; box-shadow:0 1px 3px rgba(0,0,0,.06); }
.confirm-section-title { display:flex; align-items:center; gap:8px; font-size:14px; font-weight:700; color:#0f172a; margin:0 0 16px; }
.date-strip { display:flex; gap:8px; overflow-x:auto; padding-bottom:4px; scrollbar-width:none; }
.date-strip::-webkit-scrollbar { display:none; }
.date-pill { display:flex; flex-direction:column; align-items:center; gap:2px; padding:10px 14px; border-radius:14px; border:1.5px solid #e2e8f0; background:#fff; cursor:pointer; min-width:60px; transition:all .2s; }
.date-pill:hover { border-color:#93c5fd; }
.date-pill.active { background:#2563eb; border-color:#2563eb; color:#fff; }
.date-pill-day   { font-size:10px; font-weight:600; opacity:.7; }
.date-pill-num   { font-size:18px; font-weight:800; line-height:1; }
.date-pill-month { font-size:10px; font-weight:500; opacity:.7; }
.period-label { font-size:12px; font-weight:600; color:#475569; margin:14px 0 8px; display:flex; align-items:center; gap:6px; }
.slots-grid { display:flex; flex-wrap:wrap; gap:8px; }
.slot-btn { padding:7px 14px; border-radius:10px; border:1.5px solid #e2e8f0; background:#fff; font-size:13px; font-weight:500; color:#374151; cursor:pointer; transition:all .2s; }
.slot-btn:hover:not(:disabled) { border-color:#93c5fd; color:#1d4ed8; }
.slot-btn.active { background:#2563eb; border-color:#2563eb; color:#fff; font-weight:700; }
.slot-btn.taken  { background:#f1f5f9; color:#94a3b8; cursor:not-allowed; text-decoration:line-through; }
.motif-input { width:100%; border:1.5px solid #e2e8f0; border-radius:12px; padding:10px 14px; font-size:14px; color:#0f172a; resize:none; font-family:inherit; outline:none; box-sizing:border-box; }
.motif-input:focus { border-color:#2563eb; }
.booking-summary { background:#f8fafc; border-radius:14px; padding:16px; margin-top:16px; }
.summary-title { font-size:11px; font-weight:700; color:#94a3b8; text-transform:uppercase; letter-spacing:.08em; margin:0 0 10px; }
.summary-row { display:flex; justify-content:space-between; padding:5px 0; font-size:13px; color:#64748b; border-bottom:1px solid #f1f5f9; }
.summary-row:last-child { border-bottom:none; }
.summary-row strong { color:#0f172a; }
.tarif-highlight { color:#2563eb; }

/* ── Footer steps ── */
.step-footer { display:flex; gap:12px; margin-top:20px; }
.btn-back { padding:10px 20px; background:#f1f5f9; border:none; border-radius:12px; font-size:14px; font-weight:600; color:#64748b; cursor:pointer; transition:background .2s; }
.btn-back:hover { background:#e2e8f0; }
.step-next { flex:1; max-width:260px; }

/* ── Confirmed ── */
.confirmed-full { background:#fff; border-radius:20px; padding:40px; text-align:center; box-shadow:0 1px 3px rgba(0,0,0,.06); display:flex; flex-direction:column; align-items:center; gap:16px; }
.confirmed-icon-lg { width:80px; height:80px; background:#dcfce7; border-radius:50%; display:flex; align-items:center; justify-content:center; }
.confirmed-full h3 { font-size:22px; font-weight:800; color:#0f172a; margin:0; }
.confirmed-summary { background:#f8fafc; border-radius:16px; padding:20px; width:100%; max-width:400px; }
.cs-row { display:flex; justify-content:space-between; padding:6px 0; font-size:14px; color:#64748b; border-bottom:1px solid #f1f5f9; }
.cs-row:last-child { border-bottom:none; }
.cs-row strong { color:#0f172a; }
.btn-secondary-full { background:none; border:none; color:#2563eb; font-size:14px; font-weight:600; cursor:pointer; text-decoration:underline; }

/* ── Info card ── */
.info-card { background:#fff; border-radius:20px; padding:40px; text-align:center; box-shadow:0 1px 3px rgba(0,0,0,.06); }
.info-card h4 { font-size:18px; font-weight:700; color:#0f172a; margin:0 0 8px; }
.info-card p  { font-size:14px; color:#64748b; max-width:360px; margin:0 auto; }

/* ── Search bar ville ── */
.search-bar { display:flex; align-items:center; gap:10px; background:#fff; border:1.5px solid #e2e8f0; border-radius:12px; padding:10px 14px; transition:border-color .2s; }
.search-bar:focus-within { border-color:#2563eb; }
.search-bar input { flex:1; border:none; outline:none; font-size:14px; color:#0f172a; background:transparent; }

/* ── Specialty pills (région filter) ── */
.specialty-scroll { display:flex; gap:8px; overflow-x:auto; padding-bottom:4px; scrollbar-width:none; }
.specialty-scroll::-webkit-scrollbar { display:none; }
.specialty-pill { display:flex; align-items:center; gap:6px; white-space:nowrap; padding:6px 14px; border-radius:20px; border:1.5px solid #e2e8f0; background:#fff; color:#64748b; font-size:12px; font-weight:500; cursor:pointer; transition:all .2s; }
.specialty-pill:hover { border-color:#93c5fd; color:#1d4ed8; }
.specialty-pill.active { background:#2563eb; border-color:#2563eb; color:#fff; }

/* ── Empty state ── */
.empty-state { text-align:center; color:#94a3b8; padding:32px; font-size:14px; }

/* ── btn-primary ── */
.btn-primary { display:flex; align-items:center; justify-content:center; gap:8px; width:100%; padding:12px 24px; background:linear-gradient(135deg,#2563eb,#1d4ed8); color:#fff; border:none; border-radius:12px; font-size:14px; font-weight:700; cursor:pointer; transition:opacity .2s; }
.btn-primary:hover:not(:disabled) { opacity:.9; }
.btn-primary:disabled { opacity:.5; cursor:not-allowed; }
</style>
