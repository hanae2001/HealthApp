<template>
  <div class="dash-root">

    <!-- SIDEBAR -->
    <aside class="sidebar" :class="{ 'sidebar-open': sidebarOpen }">
      <div class="sb-brand">
        <div class="sb-logo">
          <svg width="18" height="18" fill="none" stroke="white" stroke-width="2" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" d="M4.318 6.318a4.5 4.5 0 000 6.364L12 20.364l7.682-7.682a4.5 4.5 0 00-6.364-6.364L12 7.636l-1.318-1.318a4.5 4.5 0 00-6.364 0z"/>
          </svg>
        </div>
        <div>
          <p class="sb-appname">AppSanté</p>
          <p class="sb-tagline">{{ domaineEmoji(profil?.nomDomaine) }} {{ profil?.nomDomaine || 'Espace Médecin' }}</p>
        </div>
      </div>

      <p class="sb-section-lbl">NAVIGATION</p>
      <nav class="sb-nav">
        <button v-for="item in navItems" :key="item.key"
          :class="['sb-item', { active: activeNav === item.key }]"
          @click="activeNav = item.key; sidebarOpen = false">
          <span class="sb-item-icon" v-html="item.icon"></span>
          <span class="sb-item-lbl">{{ item.label }}</span>
          <span v-if="item.badge" class="sb-count">{{ item.badge }}</span>
        </button>
      </nav>

      <div style="flex:1"></div>

      <p class="sb-section-lbl">COMPTE</p>
      <div class="sb-nav" style="margin-bottom:12px">
        <button class="sb-item" @click="logout">
          <span class="sb-item-icon">
            <svg width="16" height="16" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" d="M17 16l4-4m0 0l-4-4m4 4H7m6 4v1a3 3 0 01-3 3H6a3 3 0 01-3-3V7a3 3 0 013-3h4a3 3 0 013 3v1"/></svg>
          </span>
          <span class="sb-item-lbl">Déconnexion</span>
        </button>
      </div>

      <div class="sb-usercard">
        <div class="sb-useravatar" :style="`background:${avatarColor}`">{{ userInitials }}</div>
        <div class="sb-userinfo">
          <p class="sb-username">Dr. {{ userName }}</p>
          <div class="sb-online"><span class="sb-online-dot"></span><span>En ligne</span></div>
        </div>
      </div>
    </aside>

    <div v-if="sidebarOpen" class="sidebar-overlay" @click="sidebarOpen = false"></div>

    <!-- MAIN -->
    <main class="main-content">
      <header class="topbar">
        <button class="topbar-menu" @click="sidebarOpen = !sidebarOpen">
          <svg width="20" height="20" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" d="M4 6h16M4 12h16M4 18h16"/></svg>
        </button>
        <div class="topbar-title">{{ navItems.find(n => n.key === activeNav)?.label }}</div>
        <div style="width:36px"></div>
      </header>

      <!-- PAGE: Tableau de bord -->
      <div v-if="activeNav === 'home'" class="page-content">

        <div v-if="loading" style="text-align:center;padding:60px;color:#64748b">Chargement…</div>

        <template v-else>
          <!-- Bannière de bienvenue -->
          <div style="background:linear-gradient(135deg,#2563eb 0%,#1d4ed8 100%);border-radius:16px;padding:28px 32px;color:#fff;margin-bottom:24px;display:flex;align-items:center;gap:20px">
            <div style="width:56px;height:56px;border-radius:50%;background:rgba(255,255,255,.2);display:flex;align-items:center;justify-content:center;font-size:24px;font-weight:700;flex-shrink:0">
              {{ userInitials }}
            </div>
            <div>
              <h2 style="margin:0;font-size:22px;font-weight:700">Bonjour, Dr. {{ profil?.prenom || userRaw.prenom }} {{ profil?.nom || userRaw.nom }} 👋</h2>
              <p style="margin:6px 0 0;opacity:.9;font-size:15px;font-weight:600">
                {{ domaineEmoji(profil?.nomDomaine) }} {{ profil?.nomDomaine || '—' }}
              </p>
              <p style="margin:4px 0 0;opacity:.75;font-size:13px">🏥 {{ profil?.nomEtablissement }} · {{ profil?.nomSecteur }} · {{ profil?.nomVille }}</p>
            </div>
          </div>

          <!-- Stats -->
          <div class="stats-grid" style="margin-bottom:28px;grid-template-columns:repeat(auto-fill,minmax(180px,1fr))">
            <div class="stat-tile">
              <div class="stat-tile-icon" style="background:#eff6ff">
                <svg width="20" height="20" fill="none" stroke="#2563eb" stroke-width="2" viewBox="0 0 24 24"><rect x="3" y="4" width="18" height="18" rx="2"/><path stroke-linecap="round" d="M16 2v4M8 2v4M3 10h18"/></svg>
              </div>
              <p class="stat-tile-val">{{ rdvStats.today }}</p>
              <p class="stat-tile-lbl">RDV aujourd'hui</p>
            </div>
            <div class="stat-tile">
              <div class="stat-tile-icon" style="background:#f0fdf4">
                <svg width="20" height="20" fill="none" stroke="#16a34a" stroke-width="2" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" d="M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0z"/></svg>
              </div>
              <p class="stat-tile-val">{{ rdvStats.week }}</p>
              <p class="stat-tile-lbl">Patients cette semaine</p>
            </div>
            <div class="stat-tile">
              <div class="stat-tile-icon" style="background:#fef3c7">
                <svg width="20" height="20" fill="none" stroke="#d97706" stroke-width="2" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z"/></svg>
              </div>
              <p class="stat-tile-val">{{ rdvStats.pending }}</p>
              <p class="stat-tile-lbl">RDV en attente</p>
            </div>
            <div class="stat-tile">
              <div class="stat-tile-icon" style="background:#fdf4ff">
                <svg width="20" height="20" fill="none" stroke="#9333ea" stroke-width="2" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" d="M11.049 2.927c.3-.921 1.603-.921 1.902 0l1.519 4.674a1 1 0 00.95.69h4.915c.969 0 1.371 1.24.588 1.81l-3.976 2.888a1 1 0 00-.363 1.118l1.518 4.674c.3.922-.755 1.688-1.538 1.118l-3.976-2.888a1 1 0 00-1.176 0l-3.976 2.888c-.783.57-1.838-.197-1.538-1.118l1.518-4.674a1 1 0 00-.363-1.118l-3.976-2.888c-.784-.57-.38-1.81.588-1.81h4.914a1 1 0 00.951-.69l1.519-4.674z"/></svg>
              </div>
              <p class="stat-tile-val">{{ profil?.tarifConsultation ? profil.tarifConsultation + ' DH' : '—' }}</p>
              <p class="stat-tile-lbl">Tarif consultation</p>
            </div>
            <div class="stat-tile">
              <div class="stat-tile-icon" style="background:#f0fdf4;font-size:22px;line-height:1">
                {{ domaineEmoji(profil?.nomDomaine) }}
              </div>
              <div>
                <p class="stat-tile-val" style="font-size:14px;line-height:1.3">{{ profil?.nomDomaine || '—' }}</p>
                <p class="stat-tile-lbl">Spécialité</p>
              </div>
            </div>
          </div>

          <!-- RDV à venir -->
          <div style="background:#fff;border-radius:12px;border:1.5px solid #e2e8f0;padding:20px 24px;margin-bottom:20px">
            <div style="display:flex;align-items:center;justify-content:space-between;margin-bottom:16px">
              <h4 style="margin:0;font-size:15px;font-weight:700;color:#1e293b">📅 Prochains rendez-vous</h4>
              <button @click="activeNav='rdv'" style="font-size:12px;color:#2563eb;background:none;border:none;cursor:pointer;font-weight:600">Voir tout →</button>
            </div>
            <div v-if="rdvsUpcoming.length === 0" style="text-align:center;padding:20px;color:#94a3b8;font-size:13px">
              Aucun rendez-vous à venir.
            </div>
            <div v-else style="display:grid;gap:8px">
              <div v-for="r in rdvsUpcoming" :key="r.idRdv"
                style="display:flex;align-items:center;gap:12px;padding:10px 14px;background:#f8fafc;border-radius:10px">
                <div style="width:36px;height:36px;border-radius:50%;display:flex;align-items:center;justify-content:center;font-size:12px;font-weight:700;color:#fff;flex-shrink:0"
                  :style="{background: pColor(r.nomPatient)}">
                  {{ pInitials(r.prenomPatient, r.nomPatient) }}
                </div>
                <div style="flex:1">
                  <p style="margin:0;font-size:13px;font-weight:600;color:#1e293b">{{ r.prenomPatient }} {{ r.nomPatient }}</p>
                  <p style="margin:2px 0 0;font-size:11px;color:#64748b">{{ r.dateHeure }}</p>
                </div>
                <span :class="['statut-badge', 'statut-' + (r.statut||'planifie')]">{{ statutLabel(r.statut) }}</span>
              </div>
            </div>
          </div>

          <!-- Mes disponibilités (résumé) -->
          <div style="background:#fff;border-radius:12px;border:1.5px solid #e2e8f0;padding:20px 24px">
            <div style="display:flex;align-items:center;justify-content:space-between;margin-bottom:16px">
              <h4 style="margin:0;font-size:15px;font-weight:700;color:#1e293b">🕐 Mes disponibilités</h4>
              <button @click="activeNav='dispos'" style="font-size:12px;color:#2563eb;background:none;border:none;cursor:pointer;font-weight:600">Gérer →</button>
            </div>
            <div v-if="dispos.length === 0" style="text-align:center;padding:16px;color:#94a3b8;font-size:13px">
              Aucun créneau défini. <span @click="activeNav='dispos'" style="color:#2563eb;cursor:pointer;font-weight:600">Ajouter →</span>
            </div>
            <div v-else style="display:flex;flex-wrap:wrap;gap:8px">
              <div v-for="j in JOURS.filter(j => disposByJour[j.val]?.length)" :key="j.val"
                style="background:#eff6ff;border-radius:8px;padding:8px 12px">
                <p style="margin:0;font-size:11px;font-weight:700;color:#2563eb">{{ j.label }}</p>
                <p v-for="d in disposByJour[j.val]" :key="d.idDispo" style="margin:2px 0 0;font-size:12px;color:#1e293b">
                  {{ d.heureDebut }} – {{ d.heureFin }}
                </p>
              </div>
            </div>
          </div>
        </template>
      </div>

      <!-- PAGE: Mon profil -->
      <div v-else-if="activeNav === 'profil'" class="page-content">
        <div style="max-width:640px">
          <h3 style="margin:0 0 20px;font-size:20px;font-weight:700;color:#1e293b">Mon profil</h3>

          <div style="background:#fff;border-radius:12px;border:1.5px solid #e2e8f0;overflow:hidden">
            <!-- Header profil -->
            <div style="background:linear-gradient(135deg,#2563eb,#1d4ed8);padding:24px;display:flex;align-items:center;gap:16px">
              <div style="width:64px;height:64px;border-radius:50%;background:rgba(255,255,255,.2);display:flex;align-items:center;justify-content:center;font-size:26px;font-weight:700;color:#fff">
                {{ userInitials }}
              </div>
              <div style="color:#fff">
                <p style="margin:0;font-size:20px;font-weight:700">Dr. {{ profil?.prenom || userRaw.prenom }} {{ profil?.nom || userRaw.nom }}</p>
                <p style="margin:6px 0 0;opacity:1;font-size:15px;font-weight:600;background:rgba(255,255,255,.15);display:inline-block;padding:3px 12px;border-radius:20px">
                  {{ domaineEmoji(profil?.nomDomaine) }} {{ profil?.nomDomaine }}
                </p>
              </div>
            </div>

            <!-- Infos -->
            <div style="padding:24px;display:grid;gap:16px">
              <div class="profil-row">
                <span class="profil-label">Email</span>
                <span class="profil-val">{{ profil?.email }}</span>
              </div>
              <div class="profil-row">
                <span class="profil-label">Téléphone</span>
                <span class="profil-val">{{ profil?.telephone || '—' }}</span>
              </div>
              <div class="profil-row">
                <span class="profil-label">Numéro d'ordre</span>
                <span class="profil-val" style="font-family:monospace">{{ profil?.numeroOrdre }}</span>
              </div>
              <div class="profil-row">
                <span class="profil-label">Spécialité</span>
                <span class="profil-val">
                  <span style="background:#eff6ff;color:#2563eb;padding:3px 10px;border-radius:20px;font-weight:700;font-size:13px">
                    {{ domaineEmoji(profil?.nomDomaine) }} {{ profil?.nomDomaine }}
                  </span>
                </span>
              </div>
              <div class="profil-row">
                <span class="profil-label">Établissement</span>
                <span class="profil-val">{{ profil?.nomEtablissement || '—' }}</span>
              </div>
              <div class="profil-row">
                <span class="profil-label">Secteur</span>
                <span class="profil-val">{{ profil?.nomSecteur || '—' }}</span>
              </div>
              <div class="profil-row">
                <span class="profil-label">Ville</span>
                <span class="profil-val">{{ profil?.nomVille || '—' }}</span>
              </div>
              <div class="profil-row">
                <span class="profil-label">Tarif consultation</span>
                <span class="profil-val" style="font-weight:600;color:#2563eb">{{ profil?.tarifConsultation ? profil.tarifConsultation + ' DH' : '—' }}</span>
              </div>
              <div class="profil-row">
                <span class="profil-label">Statut</span>
                <span :class="['status-pill', profil?.actif ? 'pill-active' : 'pill-inactive']">{{ profil?.actif ? 'Actif' : 'Inactif' }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- PAGE: Mes rendez-vous -->
      <div v-else-if="activeNav === 'rdv'" class="page-content">
        <div style="display:flex;align-items:center;justify-content:space-between;margin-bottom:20px">
          <h3 style="margin:0;font-size:20px;font-weight:700;color:#1e293b">Mes rendez-vous</h3>
          <button class="btn-refresh" @click="fetchRdvs()" :disabled="loadingRdv">
            <svg width="14" height="14" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" d="M4 4v5h5M20 20v-5h-5M4 9a8 8 0 0114.93-3M20 15a8 8 0 01-14.93 3"/></svg>
            Actualiser
          </button>
        </div>

        <!-- Filtres -->
        <div style="display:flex;gap:8px;flex-wrap:wrap;margin-bottom:16px">
          <button v-for="f in rdvFilters" :key="f.key"
            :class="['filter-btn', { active: rdvFilter === f.key }]"
            @click="rdvFilter = f.key">
            {{ f.label }}
            <span class="filter-count">{{ rdvCount(f.key) }}</span>
          </button>
        </div>

        <div v-if="loadingRdv" style="text-align:center;padding:40px;color:#94a3b8">Chargement…</div>

        <div v-else-if="filteredMedRdvs.length === 0" class="empty-state" style="margin-top:24px">
          <svg width="48" height="48" fill="none" stroke="#cbd5e1" stroke-width="1.5" viewBox="0 0 24 24"><rect x="3" y="4" width="18" height="18" rx="2"/><path stroke-linecap="round" d="M16 2v4M8 2v4M3 10h18"/></svg>
          <p>Aucun rendez-vous {{ rdvFilter !== 'tous' ? 'dans cette catégorie' : '' }}.</p>
        </div>

        <div v-else style="display:grid;gap:12px">
          <div v-for="rdv in filteredMedRdvs" :key="rdv.idRdv"
            style="background:#fff;border-radius:12px;border:1.5px solid #e2e8f0;padding:16px 20px;display:flex;align-items:center;gap:16px">

            <!-- Avatar patient -->
            <div style="width:44px;height:44px;border-radius:50%;display:flex;align-items:center;justify-content:center;font-size:15px;font-weight:700;color:#fff;flex-shrink:0"
              :style="{background: pColor(rdv.nomPatient)}">
              {{ pInitials(rdv.prenomPatient, rdv.nomPatient) }}
            </div>

            <!-- Info -->
            <div style="flex:1;min-width:0">
              <div style="display:flex;align-items:center;gap:8px;flex-wrap:wrap">
                <span style="font-size:15px;font-weight:700;color:#1e293b">{{ rdv.prenomPatient }} {{ rdv.nomPatient }}</span>
                <span :class="['statut-badge', 'statut-' + (rdv.statut || 'planifie')]">{{ statutLabel(rdv.statut) }}</span>
              </div>
              <div style="display:flex;gap:16px;margin-top:4px;flex-wrap:wrap">
                <span style="font-size:12px;color:#64748b">📅 {{ rdv.dateHeure }}</span>
                <span v-if="rdv.motif" style="font-size:12px;color:#64748b">💬 {{ rdv.motif }}</span>
              </div>
              <div v-if="rdv.notes" style="margin-top:6px;font-size:12px;color:#7c3aed;background:#faf5ff;border-radius:6px;padding:4px 10px;display:inline-block">
                📝 {{ rdv.notes }}
              </div>
            </div>

            <!-- Durée -->
            <div style="text-align:center;flex-shrink:0">
              <span style="font-size:12px;color:#64748b;background:#f1f5f9;padding:4px 10px;border-radius:20px">{{ rdv.duree || 30 }} min</span>
            </div>
          </div>
        </div>
      </div>

      <!-- PAGE: Disponibilités -->
      <div v-else-if="activeNav === 'dispos'" class="page-content">
        <div class="page-header-row">
          <div>
            <h3 style="margin:0;font-size:20px;font-weight:700;color:#1e293b">Mes disponibilités</h3>
            <p style="margin:4px 0 0;font-size:13px;color:#64748b">Définissez vos créneaux de travail par jour</p>
          </div>
        </div>

        <div v-if="dispoAlert.show" :class="['auth-alert', `alert-${dispoAlert.type}`]" style="margin-bottom:16px">
          <span>{{ dispoAlert.message }}</span>
        </div>

        <div style="display:grid;grid-template-columns:1fr 1fr;gap:20px">

          <!-- Formulaire d'ajout -->
          <div style="background:#fff;border-radius:12px;border:1.5px solid #e2e8f0;padding:24px">
            <h4 style="margin:0 0 20px;font-size:15px;font-weight:700;color:#1e293b">➕ Ajouter un créneau</h4>

            <div style="margin-bottom:14px">
              <label style="display:block;font-size:13px;font-weight:600;color:#374151;margin-bottom:6px">Jour</label>
              <div style="display:grid;grid-template-columns:1fr 1fr;gap:8px">
                <button v-for="j in JOURS" :key="j.val"
                  @click="newDispo.jour = j.val"
                  :style="`padding:8px;border-radius:8px;font-size:13px;font-weight:600;cursor:pointer;border:1.5px solid;transition:all .15s;` +
                    (newDispo.jour === j.val
                      ? 'background:#2563eb;color:#fff;border-color:#2563eb'
                      : 'background:#f8fafc;color:#374151;border-color:#e2e8f0')">
                  {{ j.label }}
                </button>
              </div>
            </div>

            <div style="display:grid;grid-template-columns:1fr 1fr;gap:12px;margin-bottom:20px">
              <div>
                <label style="display:block;font-size:13px;font-weight:600;color:#374151;margin-bottom:6px">Heure début</label>
                <input v-model="newDispo.debut" type="time"
                  style="width:100%;padding:9px 12px;border:1.5px solid #e2e8f0;border-radius:8px;font-size:14px;outline:none;box-sizing:border-box"/>
              </div>
              <div>
                <label style="display:block;font-size:13px;font-weight:600;color:#374151;margin-bottom:6px">Heure fin</label>
                <input v-model="newDispo.fin" type="time"
                  style="width:100%;padding:9px 12px;border:1.5px solid #e2e8f0;border-radius:8px;font-size:14px;outline:none;box-sizing:border-box"/>
              </div>
            </div>

            <button @click="addDispo" :disabled="savingDispo || !newDispo.jour || !newDispo.debut || !newDispo.fin"
              class="btn-primary" style="width:100%;padding:11px">
              {{ savingDispo ? 'Enregistrement…' : 'Ajouter ce créneau' }}
            </button>
          </div>

          <!-- Liste des créneaux existants -->
          <div style="background:#fff;border-radius:12px;border:1.5px solid #e2e8f0;padding:24px">
            <h4 style="margin:0 0 16px;font-size:15px;font-weight:700;color:#1e293b">📅 Mes créneaux</h4>

            <div v-if="loadingDispos" style="text-align:center;padding:24px;color:#94a3b8;font-size:13px">Chargement…</div>
            <div v-else-if="dispos.length === 0" style="text-align:center;padding:24px;color:#94a3b8;font-size:13px">
              Aucun créneau défini.<br>Ajoutez vos horaires de travail.
            </div>
            <div v-else style="display:grid;gap:8px">
              <div v-for="j in JOURS" :key="j.val">
                <div v-if="disposByJour[j.val]?.length">
                  <p style="font-size:11px;font-weight:700;color:#64748b;letter-spacing:.05em;margin:8px 0 6px">{{ j.label.toUpperCase() }}</p>
                  <div v-for="d in disposByJour[j.val]" :key="d.idDispo"
                    style="display:flex;align-items:center;justify-content:space-between;padding:9px 12px;background:#f0f9ff;border-radius:8px;margin-bottom:6px">
                    <div style="display:flex;align-items:center;gap:8px">
                      <span style="font-size:16px">🕐</span>
                      <span style="font-size:14px;font-weight:600;color:#1e293b">{{ d.heureDebut }} — {{ d.heureFin }}</span>
                    </div>
                    <button @click="deleteDispo(d.idDispo)"
                      style="background:none;border:none;cursor:pointer;color:#ef4444;padding:4px;border-radius:6px;line-height:0"
                      title="Supprimer">
                      <svg width="15" height="15" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16"/></svg>
                    </button>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- PAGE: Dossiers patients -->
      <div v-else-if="activeNav === 'patients'" class="page-content">

        <!-- Vue liste des patients -->
        <template v-if="!selectedPatient">
          <div style="display:flex;align-items:center;justify-content:space-between;margin-bottom:20px">
            <h3 style="margin:0;font-size:20px;font-weight:700;color:#1e293b">Dossiers patients</h3>
            <button class="btn-refresh" @click="fetchPatients()" :disabled="loadingPatients">
              <svg width="14" height="14" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" d="M4 4v5h5M20 20v-5h-5M4 9a8 8 0 0114.93-3M20 15a8 8 0 01-14.93 3"/></svg>
              Actualiser
            </button>
          </div>

          <div v-if="loadingPatients" style="text-align:center;padding:40px;color:#94a3b8">Chargement…</div>
          <div v-else-if="patients.length === 0" class="empty-state" style="margin-top:40px">
            <svg width="48" height="48" fill="none" stroke="#cbd5e1" stroke-width="1.5" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z"/></svg>
            <p>Aucun patient consulté pour le moment.</p>
          </div>
          <div v-else style="display:grid;gap:10px">
            <button v-for="p in patients" :key="p.idPatient"
              class="patient-row" @click="openDossier(p)">
              <div :style="`width:44px;height:44px;border-radius:50%;background:${pColor(p.nom)};display:flex;align-items:center;justify-content:center;color:#fff;font-weight:700;font-size:15px;flex-shrink:0`">
                {{ pInitials(p.prenom, p.nom) }}
              </div>
              <div style="flex:1;min-width:0">
                <p style="margin:0;font-weight:600;color:#1e293b;font-size:15px">{{ p.prenom }} {{ p.nom }}</p>
                <p style="margin:2px 0 0;font-size:12px;color:#64748b">{{ p.telephone || '—' }} {{ p.groupeSanguin ? '· Groupe ' + p.groupeSanguin : '' }}</p>
              </div>
              <svg width="16" height="16" fill="none" stroke="#94a3b8" stroke-width="2" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" d="M9 5l7 7-7 7"/></svg>
            </button>
          </div>
        </template>

        <!-- Vue dossier d'un patient -->
        <template v-else>
          <div style="display:flex;align-items:center;gap:12px;margin-bottom:20px">
            <button class="btn-refresh" @click="selectedPatient = null; patientDossier = null">
              <svg width="14" height="14" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" d="M15 19l-7-7 7-7"/></svg>
              Retour
            </button>
            <div :style="`width:40px;height:40px;border-radius:50%;background:${pColor(selectedPatient.nom)};display:flex;align-items:center;justify-content:center;color:#fff;font-weight:700;font-size:14px;flex-shrink:0`">
              {{ pInitials(selectedPatient.prenom, selectedPatient.nom) }}
            </div>
            <div>
              <h3 style="margin:0;font-size:18px;font-weight:700;color:#1e293b">{{ selectedPatient.prenom }} {{ selectedPatient.nom }}</h3>
              <p style="margin:0;font-size:12px;color:#64748b">{{ selectedPatient.telephone }}</p>
            </div>
            <div style="margin-left:auto;display:flex;gap:8px">
              <button class="dos-add-btn" @click="openAddModal('note')">+ Note clinique</button>
              <button class="dos-add-btn" style="background:#059669" @click="openAddModal('soin')">+ Soin</button>
              <button class="dos-add-btn" style="background:#7c3aed" @click="openAddModal('ordonnance')">+ Ordonnance</button>
            </div>
          </div>

          <div v-if="loadingDossier" style="text-align:center;padding:40px;color:#94a3b8">Chargement du dossier…</div>
          <template v-else-if="patientDossier">
            <!-- Onglets -->
            <div style="display:flex;gap:4px;margin-bottom:16px;border-bottom:2px solid #e2e8f0;padding-bottom:0">
              <button v-for="t in dossTabs" :key="t.key"
                :class="['dos-tab', { active: dossTab === t.key }]"
                @click="dossTab = t.key">
                {{ t.icon }} {{ t.label }} <span class="dos-tab-count">{{ t.count }}</span>
              </button>
            </div>

            <!-- Notes cliniques -->
            <div v-if="dossTab === 'notes'">
              <div v-if="patientDossier.notes.length === 0" class="dos-empty">Aucune note clinique.</div>
              <div v-for="n in patientDossier.notes" :key="n.idNote" class="dos-card">
                <div class="dos-card-header">
                  <span class="dos-doc">Dr. {{ n.prenomMedecin }} {{ n.nomMedecin }}</span>
                  <span class="dos-date">📅 {{ n.dateNote }}</span>
                </div>
                <div v-if="n.diagnostic" class="dos-field"><label>Diagnostic</label><p>{{ n.diagnostic }}</p></div>
                <div v-if="n.planTraitement" class="dos-field"><label>Plan de traitement</label><p>{{ n.planTraitement }}</p></div>
                <div v-if="n.observations" class="dos-field"><label>Observations</label><p>{{ n.observations }}</p></div>
              </div>
            </div>

            <!-- Soins -->
            <div v-else-if="dossTab === 'soins'">
              <div v-if="patientDossier.soins.length === 0" class="dos-empty">Aucun soin enregistré.</div>
              <div style="display:grid;gap:10px">
                <div v-for="s in patientDossier.soins" :key="s.idSoin" class="dos-soin-row">
                  <div style="width:36px;height:36px;border-radius:10px;background:#f0fdf4;display:flex;align-items:center;justify-content:center;font-size:18px;flex-shrink:0">🩹</div>
                  <div style="flex:1;min-width:0">
                    <p style="margin:0;font-weight:600;color:#1e293b;font-size:14px">{{ s.typeSoin }}</p>
                    <p style="margin:2px 0 0;font-size:12px;color:#64748b">{{ s.dateSoin }}</p>
                  </div>
                  <p style="margin:0;font-weight:700;color:#059669;font-size:15px">{{ s.cout }} DH</p>
                </div>
              </div>
            </div>

            <!-- Ordonnances -->
            <div v-else-if="dossTab === 'ordonnances'">
              <div v-if="patientDossier.ordonnances.length === 0" class="dos-empty">Aucune ordonnance.</div>
              <div v-for="o in patientDossier.ordonnances" :key="o.idOrdonnance" class="dos-card">
                <div class="dos-card-header">
                  <div>
                    <span class="dos-doc">Dr. {{ o.prenomMedecin }} {{ o.nomMedecin }}</span>
                    <span style="font-size:11px;color:#64748b;margin-left:8px">Valable {{ o.validiteJours }}j</span>
                  </div>
                  <span class="dos-date">💊 {{ o.dateEmission }}</span>
                </div>
                <div style="display:grid;gap:6px;margin-top:10px">
                  <div v-for="med in o.medicaments" :key="med.id" class="dos-med-row">
                    <span class="dos-med-name">{{ med.nomMedicament }}</span>
                    <span v-if="med.dosage" class="dos-med-info">{{ med.dosage }}</span>
                    <span v-if="med.frequence" class="dos-med-info">{{ med.frequence }}</span>
                    <span v-if="med.duree" class="dos-med-info">{{ med.duree }}</span>
                  </div>
                </div>
                <p v-if="o.notes" style="margin:10px 0 0;font-size:12px;color:#64748b;font-style:italic">{{ o.notes }}</p>
              </div>
            </div>
          </template>
        </template>

        <!-- Modal ajout -->
        <Teleport to="body">
          <div v-if="addModal.show" class="modal-overlay" @click.self="addModal.show = false">
            <div class="modal-box" style="max-width:520px">
              <div class="modal-header">
                <p class="modal-title">
                  {{ addModal.type === 'note' ? '🩺 Nouvelle note clinique' : addModal.type === 'soin' ? '🩹 Nouveau soin' : '💊 Nouvelle ordonnance' }}
                </p>
                <button class="modal-close" @click="addModal.show = false">&#x2715;</button>
              </div>

              <!-- Formulaire Note -->
              <div v-if="addModal.type === 'note'" style="padding:20px 24px;display:grid;gap:14px">
                <div><label class="form-lbl">Diagnostic</label><textarea v-model="addModal.diagnostic" class="form-inp" rows="2" style="width:100%;resize:vertical" placeholder="Diagnostic…"></textarea></div>
                <div><label class="form-lbl">Plan de traitement</label><textarea v-model="addModal.planTraitement" class="form-inp" rows="2" style="width:100%;resize:vertical" placeholder="Plan…"></textarea></div>
                <div><label class="form-lbl">Observations</label><textarea v-model="addModal.observations" class="form-inp" rows="2" style="width:100%;resize:vertical" placeholder="Observations…"></textarea></div>
                <div style="display:flex;gap:10px;justify-content:flex-end">
                  <button class="subform-back" @click="addModal.show = false">Annuler</button>
                  <button class="subform-submit" :disabled="addModal.saving" @click="saveNote()">{{ addModal.saving ? 'Enregistrement…' : 'Enregistrer' }}</button>
                </div>
              </div>

              <!-- Formulaire Soin -->
              <div v-else-if="addModal.type === 'soin'" style="padding:20px 24px;display:grid;gap:14px">
                <div><label class="form-lbl">Type de soin *</label><input v-model="addModal.typeSoin" class="form-inp" style="width:100%" placeholder="Ex: Consultation, Radio, Nettoyage…"/></div>
                <div style="display:flex;gap:10px">
                  <div style="flex:1"><label class="form-lbl">Date *</label><input v-model="addModal.dateSoin" type="date" class="form-inp" style="width:100%"/></div>
                  <div style="flex:1"><label class="form-lbl">Coût (DH)</label><input v-model="addModal.cout" type="number" min="0" class="form-inp" style="width:100%" placeholder="0"/></div>
                </div>
                <div style="display:flex;gap:10px;justify-content:flex-end">
                  <button class="subform-back" @click="addModal.show = false">Annuler</button>
                  <button class="subform-submit" :disabled="addModal.saving" @click="saveSoin()">{{ addModal.saving ? 'Enregistrement…' : 'Enregistrer' }}</button>
                </div>
              </div>

              <!-- Formulaire Ordonnance -->
              <div v-else-if="addModal.type === 'ordonnance'" style="padding:20px 24px">
                <div style="display:grid;gap:12px;margin-bottom:16px">
                  <div v-for="(med, i) in addModal.medicaments" :key="i" style="background:#f8fafc;border-radius:10px;padding:12px;display:grid;gap:8px">
                    <div style="display:flex;align-items:center;justify-content:space-between">
                      <label class="form-lbl" style="margin:0">Médicament {{ i+1 }}</label>
                      <button v-if="i > 0" @click="addModal.medicaments.splice(i,1)" style="background:none;border:none;color:#dc2626;cursor:pointer;font-size:16px">✕</button>
                    </div>
                    <input v-model="med.nomMedicament" class="form-inp" style="width:100%" placeholder="Nom du médicament *"/>
                    <div style="display:grid;grid-template-columns:1fr 1fr 1fr;gap:8px">
                      <input v-model="med.dosage" class="form-inp" placeholder="Dosage"/>
                      <input v-model="med.frequence" class="form-inp" placeholder="Fréquence"/>
                      <input v-model="med.duree" class="form-inp" placeholder="Durée"/>
                    </div>
                  </div>
                  <button @click="addModal.medicaments.push({nomMedicament:'',dosage:'',frequence:'',duree:''})"
                    style="border:1.5px dashed #cbd5e1;background:#fff;border-radius:10px;padding:10px;color:#64748b;font-size:13px;cursor:pointer">
                    + Ajouter un médicament
                  </button>
                </div>
                <div><label class="form-lbl">Notes</label><textarea v-model="addModal.ordNotes" class="form-inp" rows="2" style="width:100%;resize:vertical" placeholder="Instructions générales…"></textarea></div>
                <div style="display:flex;gap:10px;justify-content:flex-end;margin-top:14px">
                  <button class="subform-back" @click="addModal.show = false">Annuler</button>
                  <button class="subform-submit" :disabled="addModal.saving" @click="saveOrdonnance()">{{ addModal.saving ? 'Enregistrement…' : 'Enregistrer' }}</button>
                </div>
              </div>
            </div>
          </div>
        </Teleport>
      </div>

    </main>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const API = 'http://localhost:8081/api/medecin'
const token = localStorage.getItem('token')
const userRaw = JSON.parse(localStorage.getItem('user') || '{}')

const activeNav   = ref('home')
const sidebarOpen = ref(false)
const loading     = ref(false)
const profil      = ref(null)

// ── Disponibilités ────────────────────────────────────────────
const JOURS = [
  { val: 'lundi',    label: 'Lundi' },
  { val: 'mardi',    label: 'Mardi' },
  { val: 'mercredi', label: 'Mercredi' },
  { val: 'jeudi',    label: 'Jeudi' },
  { val: 'vendredi', label: 'Vendredi' },
  { val: 'samedi',   label: 'Samedi' },
]

const dispos        = ref([])
const loadingDispos = ref(false)
const savingDispo   = ref(false)
const dispoAlert    = ref({ show: false, type: 'error', message: '' })
const newDispo      = ref({ jour: '', debut: '', fin: '' })

const disposByJour  = computed(() => {
  const map = {}
  dispos.value.forEach(d => {
    if (!map[d.jourSemaine]) map[d.jourSemaine] = []
    map[d.jourSemaine].push(d)
  })
  return map
})

function showDispoAlert(msg, type = 'error') {
  dispoAlert.value = { show: true, type, message: msg }
  setTimeout(() => dispoAlert.value.show = false, 3500)
}

async function fetchDispos() {
  loadingDispos.value = true
  try {
    const res = await fetch(`${API}/disponibilites`, { headers: hdrs() })
    dispos.value = res.ok ? await res.json() : []
  } finally {
    loadingDispos.value = false
  }
}

async function addDispo() {
  if (!newDispo.value.jour || !newDispo.value.debut || !newDispo.value.fin) return
  savingDispo.value = true
  try {
    const res = await fetch(`${API}/disponibilites`, {
      method: 'POST', headers: hdrs(),
      body: JSON.stringify({ jourSemaine: newDispo.value.jour, heureDebut: newDispo.value.debut, heureFin: newDispo.value.fin })
    })
    if (res.ok) {
      const d = await res.json()
      dispos.value = [...dispos.value, d]
      newDispo.value = { jour: '', debut: '', fin: '' }
      showDispoAlert('Créneau ajouté avec succès', 'success')
    } else if (res.status === 409) {
      showDispoAlert('Ce créneau existe déjà', 'error')
    } else {
      showDispoAlert('Heure de fin doit être après heure de début', 'error')
    }
  } finally {
    savingDispo.value = false
  }
}

async function deleteDispo(id) {
  const res = await fetch(`${API}/disponibilites/${id}`, { method: 'DELETE', headers: hdrs() })
  if (res.ok) {
    dispos.value = dispos.value.filter(d => d.idDispo !== id)
    showDispoAlert('Créneau supprimé', 'success')
  }
}

const navItems = [
  { key: 'home',     label: 'Tableau de bord', icon: '<svg width="18" height="18" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" d="M3 12l2-2m0 0l7-7 7 7M5 10v10a1 1 0 001 1h3m10-11l2 2m-2-2v10a1 1 0 01-1 1h-3m-6 0a1 1 0 001-1v-4a1 1 0 011-1h2a1 1 0 011 1v4a1 1 0 001 1m-6 0h6"/></svg>' },
  { key: 'rdv',      label: 'Mes rendez-vous',  icon: '<svg width="18" height="18" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24"><rect x="3" y="4" width="18" height="18" rx="2"/><path stroke-linecap="round" d="M16 2v4M8 2v4M3 10h18"/></svg>' },
  { key: 'dispos',   label: 'Disponibilités',   icon: '<svg width="18" height="18" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z"/></svg>' },
  { key: 'patients', label: 'Dossiers patients', icon: '<svg width="18" height="18" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z"/></svg>' },
  { key: 'profil',   label: 'Mon profil',        icon: '<svg width="18" height="18" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z"/></svg>' },
]

const userName     = computed(() => `${userRaw.prenom || ''} ${userRaw.nom || ''}`.trim() || userRaw.email || '')
const userInitials = computed(() => {
  const n = userRaw.nom || ''; const p = userRaw.prenom || ''
  return ((p[0] || '') + (n[0] || '')).toUpperCase() || '?'
})
const avatarColors = ['#2563eb','#7c3aed','#0891b2','#be185d','#059669']
const avatarColor  = computed(() => avatarColors[(userRaw.nom?.charCodeAt(0) || 0) % avatarColors.length])

// ── RDV médecin ───────────────────────────────────────────────
const rdvsMedecin  = ref([])
const loadingRdv   = ref(false)
const rdvFilter    = ref('tous')

const rdvFilters = [
  { key: 'tous',     label: 'Tous' },
  { key: 'planifie', label: 'Planifiés' },
  { key: 'confirme', label: 'Confirmés' },
  { key: 'reporte',  label: 'Reportés' },
  { key: 'termine',  label: 'Terminés' },
]

const STATUT_LABELS = { planifie:'Planifié', confirme:'Confirmé', annule:'Annulé', termine:'Terminé', refuse:'Refusé', reporte:'Reporté' }
function statutLabel(s) { return STATUT_LABELS[s] || s }

const filteredMedRdvs = computed(() =>
  rdvFilter.value === 'tous' ? rdvsMedecin.value : rdvsMedecin.value.filter(r => r.statut === rdvFilter.value)
)

function rdvCount(key) {
  return key === 'tous' ? rdvsMedecin.value.length : rdvsMedecin.value.filter(r => r.statut === key).length
}

const rdvsUpcoming = computed(() =>
  rdvsMedecin.value.filter(r => r.statut === 'planifie' || r.statut === 'confirme').slice(0, 4)
)

const rdvStats = computed(() => {
  const now = new Date()
  const todayStr = now.toLocaleDateString('fr-FR', { day:'2-digit', month:'2-digit', year:'numeric' })
  const weekStart = new Date(now); weekStart.setDate(now.getDate() - now.getDay())
  return {
    today:   rdvsMedecin.value.filter(r => r.dateHeure?.startsWith(todayStr)).length,
    week:    rdvsMedecin.value.filter(r => {
      if (!r.dateHeure) return false
      const [d,m,y] = r.dateHeure.split(/[/ ]/);
      const dt = new Date(`${y}-${m}-${d}`)
      return dt >= weekStart && dt <= now
    }).length,
    pending: rdvsMedecin.value.filter(r => r.statut === 'planifie').length,
  }
})

function pInitials(prenom, nom) { return ((prenom?.[0]||'') + (nom?.[0]||'')).toUpperCase() || '?' }
function pColor(nom) { return avatarColors[(nom?.charCodeAt(0) || 0) % avatarColors.length] }

async function fetchRdvs() {
  loadingRdv.value = true
  try {
    const res = await fetch(`${API}/rdv`, { headers: hdrs() })
    rdvsMedecin.value = res.ok ? await res.json() : []
  } finally {
    loadingRdv.value = false
  }
}

// ── Dossiers patients ────────────────────────────────────────
const patients        = ref([])
const loadingPatients = ref(false)
const selectedPatient = ref(null)
const patientDossier  = ref(null)
const loadingDossier  = ref(false)
const dossTab         = ref('notes')

const dossTabs = computed(() => [
  { key: 'notes',       label: 'Notes cliniques', icon: '🩺', count: patientDossier.value?.notes?.length || 0 },
  { key: 'soins',       label: 'Soins',           icon: '🩹', count: patientDossier.value?.soins?.length || 0 },
  { key: 'ordonnances', label: 'Ordonnances',     icon: '💊', count: patientDossier.value?.ordonnances?.length || 0 },
])

const addModal = ref({
  show: false, type: null, saving: false,
  diagnostic: '', planTraitement: '', observations: '',
  typeSoin: '', dateSoin: '', cout: 0,
  medicaments: [{ nomMedicament: '', dosage: '', frequence: '', duree: '' }],
  ordNotes: ''
})

async function fetchPatients() {
  loadingPatients.value = true
  try {
    const res = await fetch(`${API}/patients`, { headers: hdrs() })
    patients.value = res.ok ? await res.json() : []
  } finally { loadingPatients.value = false }
}

async function openDossier(p) {
  selectedPatient.value = p
  dossTab.value = 'notes'
  loadingDossier.value = true
  try {
    const res = await fetch(`${API}/patient/${p.idPatient}/dossier`, { headers: hdrs() })
    patientDossier.value = res.ok ? await res.json() : null
  } finally { loadingDossier.value = false }
}

function openAddModal(type) {
  addModal.value = {
    show: true, type, saving: false,
    diagnostic: '', planTraitement: '', observations: '',
    typeSoin: '', dateSoin: new Date().toISOString().split('T')[0], cout: 0,
    medicaments: [{ nomMedicament: '', dosage: '', frequence: '', duree: '' }],
    ordNotes: ''
  }
}

async function saveNote() {
  addModal.value.saving = true
  try {
    const res = await fetch(`${API}/patient/${selectedPatient.value.idPatient}/notes`, {
      method: 'POST', headers: hdrs(),
      body: JSON.stringify({ diagnostic: addModal.value.diagnostic, planTraitement: addModal.value.planTraitement, observations: addModal.value.observations })
    })
    if (res.ok) {
      patientDossier.value.notes.unshift(await res.json())
      addModal.value.show = false; dossTab.value = 'notes'
    } else {
      const msg = await res.text().catch(() => res.status)
      alert('Erreur ' + res.status + ' : ' + msg)
    }
  } catch(e) { alert('Erreur réseau : ' + e.message) }
  finally { addModal.value.saving = false }
}

async function saveSoin() {
  if (!addModal.value.typeSoin) { alert('Saisissez le type de soin'); return }
  addModal.value.saving = true
  try {
    const res = await fetch(`${API}/patient/${selectedPatient.value.idPatient}/soins`, {
      method: 'POST', headers: hdrs(),
      body: JSON.stringify({ typeSoin: addModal.value.typeSoin, dateSoin: addModal.value.dateSoin, cout: Number(addModal.value.cout) || 0 })
    })
    if (res.ok) {
      patientDossier.value.soins.unshift(await res.json())
      addModal.value.show = false; dossTab.value = 'soins'
    } else {
      const msg = await res.text().catch(() => res.status)
      alert('Erreur ' + res.status + ' : ' + msg)
    }
  } catch(e) { alert('Erreur réseau : ' + e.message) }
  finally { addModal.value.saving = false }
}

async function saveOrdonnance() {
  const meds = addModal.value.medicaments.filter(m => m.nomMedicament.trim())
  if (meds.length === 0) { alert('Ajoutez au moins un médicament'); return }
  addModal.value.saving = true
  try {
    const res = await fetch(`${API}/patient/${selectedPatient.value.idPatient}/ordonnances`, {
      method: 'POST', headers: hdrs(),
      body: JSON.stringify({ notes: addModal.value.ordNotes, medicaments: meds })
    })
    if (res.ok) {
      patientDossier.value.ordonnances.unshift(await res.json())
      addModal.value.show = false; dossTab.value = 'ordonnances'
    } else {
      const msg = await res.text().catch(() => res.status)
      alert('Erreur ' + res.status + ' : ' + msg)
    }
  } catch(e) { alert('Erreur réseau : ' + e.message) }
  finally { addModal.value.saving = false }
}

const DOMAIN_EMOJI = {
  'Généraliste':'🩺','Médecine d\'urgence':'🚑','Médecine interne':'🏥','Médecine du travail':'💼','Gériatrie':'👴',
  'Cardiologie':'❤️','Chirurgie vasculaire':'🫀','Angiologie':'🩸',
  'Neurologie':'🧠','Neurochirurgie':'🔬','Psychiatrie':'🗣️','Psychologie':'💭',
  'Pédiatrie':'👶','Néonatologie':'🍼','Gynécologie':'👩','Obstétrique':'🌸',
  'Gastroentérologie':'🫃','Hépatologie':'🫀','Néphrologie':'🫘','Urologie':'💧',
  'Orthopédie':'🦴','Rhumatologie':'🦾','Traumatologie':'🩹',
  'ORL':'👂','Ophtalmologie':'👁️','Dermatologie':'🧴',
  'Dentaire':'🦷','Orthodontie':'😁','Parodontologie':'🦷',
  'Endocrinologie':'⚗️','Diabétologie':'💉','Nutrition':'🥗',
  'Pneumologie':'🫁','Allergologie':'🌺',
  'Oncologie':'🎗️','Hématologie':'🩺',
  'Radiologie':'🩻','Biologie médicale':'🔬',
}
function domaineEmoji(nom) { return DOMAIN_EMOJI[nom] || '🏥' }

function hdrs() { return { 'Content-Type': 'application/json', Authorization: `Bearer ${token}` } }

function logout() {
  localStorage.removeItem('token')
  localStorage.removeItem('user')
  router.push('/login')
}

onMounted(async () => {
  loading.value = true
  try {
    const [profilRes] = await Promise.all([
      fetch(`${API}/profil`, { headers: hdrs() }),
      fetchDispos(),
      fetchRdvs(),
      fetchPatients()
    ])
    if (profilRes.status === 401) { router.push('/login'); return }
    profil.value = await profilRes.json()
  } finally {
    loading.value = false
  }
})
</script>

<style scoped>
.profil-row { display:flex; justify-content:space-between; align-items:center; padding:10px 0; border-bottom:1px solid #f1f5f9; }
.profil-row:last-child { border-bottom:none; }
.profil-label { font-size:13px; color:#64748b; font-weight:500; }
.profil-val { font-size:14px; color:#1e293b; font-weight:500; }

.btn-refresh { display:flex;align-items:center;gap:6px;padding:7px 14px;border-radius:8px;border:1px solid #e2e8f0;background:#fff;color:#475569;font-size:13px;font-weight:500;cursor:pointer; }
.btn-refresh:hover:not(:disabled) { background:#f8fafc; }
.btn-refresh:disabled { opacity:.5;cursor:default; }

.filter-btn { padding:6px 14px;border-radius:20px;border:1px solid #e2e8f0;background:#fff;font-size:12px;font-weight:500;color:#64748b;cursor:pointer;display:flex;align-items:center;gap:6px; }
.filter-btn.active { background:#2563eb;color:#fff;border-color:#2563eb; }
.filter-count { background:rgba(0,0,0,.08);border-radius:20px;padding:0 6px;font-size:11px; }
.filter-btn.active .filter-count { background:rgba(255,255,255,.25); }

.statut-badge { display:inline-block;padding:3px 10px;border-radius:20px;font-size:11px;font-weight:600; }
.statut-planifie,.statut-planifie { background:#eff6ff;color:#2563eb; }
.statut-confirme { background:#f0fdf4;color:#16a34a; }
.statut-annule,.statut-refuse { background:#fff1f2;color:#dc2626; }
.statut-termine { background:#f1f5f9;color:#64748b; }
.statut-reporte { background:#fff7ed;color:#ea580c; }

/* Patients / Dossier */
.patient-row { display:flex;align-items:center;justify-content:space-between;padding:12px 16px;border-radius:10px;border:1px solid #e2e8f0;margin-bottom:8px;cursor:pointer;transition:border-color .15s; }
.patient-row:hover { border-color:#2563eb; }
.patient-name { font-size:14px;font-weight:600;color:#1e293b; }
.patient-meta { font-size:12px;color:#94a3b8;margin-top:2px; }
.dos-add-btn { display:flex;align-items:center;gap:6px;padding:7px 14px;border-radius:8px;border:none;background:#2563eb;color:#fff;font-size:13px;font-weight:500;cursor:pointer; }
.dos-add-btn:hover { background:#1d4ed8; }
.dos-tab { padding:7px 16px;border-radius:8px;border:1px solid #e2e8f0;background:#fff;font-size:13px;font-weight:500;color:#64748b;cursor:pointer;display:flex;align-items:center;gap:6px; }
.dos-tab.active { background:#2563eb;color:#fff;border-color:#2563eb; }
.dos-tab-count { background:rgba(0,0,0,.08);border-radius:20px;padding:0 6px;font-size:11px; }
.dos-tab.active .dos-tab-count { background:rgba(255,255,255,.25); }
.dos-empty { text-align:center;padding:40px;color:#94a3b8;font-size:14px; }
.dos-card { border:1px solid #e2e8f0;border-radius:12px;padding:16px;margin-bottom:12px; }
.dos-card-header { display:flex;align-items:center;justify-content:space-between;margin-bottom:12px;padding-bottom:10px;border-bottom:1px solid #f1f5f9; }
.dos-doc { font-size:13px;font-weight:600;color:#1e293b; }
.dos-spec { font-size:12px;color:#94a3b8; }
.dos-date { font-size:12px;color:#64748b; }
.dos-field { margin-bottom:10px; }
.dos-field-label { font-size:11px;font-weight:600;text-transform:uppercase;letter-spacing:.5px;color:#94a3b8;margin-bottom:3px; }
.dos-field-val { font-size:14px;color:#1e293b; }
.dos-soin-row { display:flex;align-items:center;gap:12px;padding:10px 14px;border-radius:8px;background:#f8fafc;margin-bottom:8px; }
.dos-soin-type { font-size:14px;font-weight:600;color:#1e293b;flex:1; }
.dos-soin-date { font-size:12px;color:#64748b; }
.dos-soin-cout { font-size:13px;font-weight:600;color:#2563eb; }
.dos-med-row { display:flex;align-items:center;gap:10px;padding:8px 12px;border-radius:8px;background:#f8fafc;margin-bottom:6px; }
.dos-med-name { font-size:13px;font-weight:600;color:#1e293b; }
.dos-med-info { font-size:12px;color:#64748b; }

/* Modal */
.modal-overlay { position:fixed;inset:0;background:rgba(0,0,0,.5);display:flex;align-items:center;justify-content:center;z-index:1000; }
.modal-box { background:#fff;border-radius:16px;padding:24px;width:520px;max-width:95vw;max-height:90vh;overflow-y:auto; }
.modal-header { display:flex;align-items:center;justify-content:space-between;margin-bottom:20px; }
.modal-title { font-size:18px;font-weight:700;color:#1e293b; }
.modal-close { background:none;border:none;font-size:20px;color:#94a3b8;cursor:pointer;padding:4px; }
.form-lbl { display:block;font-size:13px;font-weight:500;color:#374151;margin-bottom:4px; }
.form-inp { width:100%;padding:9px 12px;border:1px solid #d1d5db;border-radius:8px;font-size:14px;color:#1e293b;outline:none;box-sizing:border-box; }
.form-inp:focus { border-color:#2563eb;box-shadow:0 0 0 3px rgba(37,99,235,.1); }
.subform-footer { display:flex;gap:10px;justify-content:flex-end;margin-top:20px; }
.subform-back { padding:9px 18px;border-radius:8px;border:1px solid #e2e8f0;background:#fff;color:#64748b;font-size:13px;font-weight:500;cursor:pointer; }
.subform-back:hover { background:#f8fafc; }
.subform-submit { padding:9px 18px;border-radius:8px;border:none;background:#2563eb;color:#fff;font-size:13px;font-weight:600;cursor:pointer; }
.subform-submit:hover:not(:disabled) { background:#1d4ed8; }
.subform-submit:disabled { opacity:.6;cursor:default; }
.med-add-btn { display:flex;align-items:center;gap:4px;padding:6px 12px;border-radius:7px;border:1px dashed #2563eb;background:none;color:#2563eb;font-size:12px;cursor:pointer;margin-top:6px; }
.med-remove-btn { background:none;border:none;color:#dc2626;font-size:16px;cursor:pointer;padding:0 4px; }
</style>
