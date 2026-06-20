<template>
  <div class="dash-root">

    <!-- ═══ SIDEBAR ═══ -->
    <aside class="sidebar" :class="{ 'sidebar-open': sidebarOpen }">
      <div class="sb-brand">
        <div class="sb-logo">
          <svg width="18" height="18" fill="none" stroke="white" stroke-width="2" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" d="M4.318 6.318a4.5 4.5 0 000 6.364L12 20.364l7.682-7.682a4.5 4.5 0 00-6.364-6.364L12 7.636l-1.318-1.318a4.5 4.5 0 00-6.364 0z"/>
          </svg>
        </div>
        <div>
          <p class="sb-appname">AppSanté</p>
          <p class="sb-tagline">Espace Admin</p>
        </div>
      </div>

      <p class="sb-section-lbl">GESTION</p>
      <nav class="sb-nav">
        <button v-for="item in navItems" :key="item.key"
          :class="['sb-item', { active: activeNav === item.key }]"
          @click="activeNav = item.key; sidebarOpen = false">
          <span class="sb-item-icon" v-html="item.icon"></span>
          <span class="sb-item-lbl">{{ item.label }}</span>
          <span v-if="item.count !== undefined" class="sb-count">{{ item.count }}</span>
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
        <div class="sb-useravatar">{{ userInitials }}</div>
        <div class="sb-userinfo">
          <p class="sb-username">{{ userName }}</p>
          <div class="sb-online"><span class="sb-online-dot"></span><span>Administrateur</span></div>
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
          <div class="user-avatar-top">{{ userInitials }}</div>
        </div>
      </header>

      <!-- ══ PAGE: Tableau de bord ══ -->
      <div v-if="activeNav === 'home'" class="page-content">
        <div class="hero-card">
          <div class="hero-left">
            <p class="hero-eyebrow">Administration</p>
            <h2 class="hero-title">Bonjour, {{ userName }}</h2>
            <p class="hero-sub">Gérez les médecins, les réceptionnistes et les établissements depuis ce panneau.</p>
          </div>
        </div>

        <div class="stats-row">
          <div class="stat-tile" @click="activeNav='medecins'" style="cursor:pointer">
            <div class="stat-tile-icon" style="background:#eff6ff">
              <svg width="20" height="20" fill="none" stroke="#2563eb" stroke-width="2" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z"/></svg>
            </div>
            <div>
              <p class="stat-tile-val">{{ medecins.length }}</p>
              <p class="stat-tile-lbl">Médecins</p>
            </div>
          </div>
          <div class="stat-tile" @click="activeNav='receptionnistes'" style="cursor:pointer">
            <div class="stat-tile-icon" style="background:#f0fdf4">
              <svg width="20" height="20" fill="none" stroke="#16a34a" stroke-width="2" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" d="M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0z"/></svg>
            </div>
            <div>
              <p class="stat-tile-val">{{ receptionnistes.length }}</p>
              <p class="stat-tile-lbl">Réceptionnistes</p>
            </div>
          </div>
          <div class="stat-tile">
            <div class="stat-tile-icon" style="background:#fff7ed">
              <svg width="20" height="20" fill="none" stroke="#ea580c" stroke-width="2" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" d="M19 21V5a2 2 0 00-2-2H7a2 2 0 00-2 2v16m14 0h2m-2 0h-5m-9 0H3m2 0h5M9 7h1m-1 4h1m4-4h1m-1 4h1"/></svg>
            </div>
            <div>
              <p class="stat-tile-val">{{ etablissements.length }}</p>
              <p class="stat-tile-lbl">Établissements</p>
            </div>
          </div>
          <div class="stat-tile">
            <div class="stat-tile-icon" style="background:#fdf4ff">
              <svg width="20" height="20" fill="none" stroke="#a21caf" stroke-width="2" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2"/></svg>
            </div>
            <div>
              <p class="stat-tile-val">{{ domaines.length }}</p>
              <p class="stat-tile-lbl">Spécialités</p>
            </div>
          </div>
        </div>

        <div class="home-2col">
          <div class="panel">
            <div class="panel-header"><h4>Actions rapides</h4></div>
            <div class="quick-list">
              <button class="quick-item" @click="activeNav='medecins'; openWizard('medecin')">
                <div class="qi-icon" style="background:#eff6ff;color:#2563eb">
                  <svg width="20" height="20" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" d="M12 4v16m8-8H4"/></svg>
                </div>
                <div style="flex:1"><p class="qi-title">Ajouter un médecin</p><p class="qi-sub">Créer un compte médecin</p></div>
                <svg width="14" height="14" fill="none" stroke="#cbd5e1" stroke-width="2.5" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" d="M9 5l7 7-7 7"/></svg>
              </button>
              <button class="quick-item" @click="activeNav='receptionnistes'; openWizard('receptionniste')">
                <div class="qi-icon" style="background:#f0fdf4;color:#16a34a">
                  <svg width="20" height="20" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" d="M12 4v16m8-8H4"/></svg>
                </div>
                <div style="flex:1"><p class="qi-title">Ajouter une réceptionniste</p><p class="qi-sub">Créer un compte réceptionniste</p></div>
                <svg width="14" height="14" fill="none" stroke="#cbd5e1" stroke-width="2.5" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" d="M9 5l7 7-7 7"/></svg>
              </button>
            </div>
          </div>

          <div class="panel">
            <div class="panel-header"><h4>Derniers médecins</h4></div>
            <div v-if="medecins.length === 0" style="color:#94a3b8;font-size:13px;padding:12px 0">Aucun médecin enregistré.</div>
            <div v-for="m in medecins.slice(0,4)" :key="m.idMedecin" class="hist-row">
              <div class="hist-avatar" :style="`background:${avatarColor(m.nom)}`">{{ initials(m.nom, m.prenom) }}</div>
              <div class="hist-info">
                <p class="hist-doc">Dr. {{ m.prenom }} {{ m.nom }}</p>
                <p class="hist-meta">{{ m.nomDomaine }} · {{ m.nomEtablissement }}</p>
              </div>
              <span class="hist-badge" :class="m.actif ? 'hbadge-done' : 'hbadge-cancel'">{{ m.actif ? 'Actif' : 'Inactif' }}</span>
            </div>
          </div>
        </div>
      </div>

      <!-- ══ PAGE: Médecins ══ -->
      <div v-else-if="activeNav === 'medecins'" class="page-content">
        <div class="page-header-row">
          <div>
            <h3 style="margin:0;font-size:20px;font-weight:700;color:#1e293b">Médecins</h3>
            <p style="margin:4px 0 0;font-size:13px;color:#64748b">{{ medecins.length }} médecin(s) — {{ medecinsByDomaine.length }} spécialité(s)</p>
          </div>
          <button class="btn-primary" style="width:auto;padding:10px 20px" @click="openMedecinModal">
            + Ajouter un médecin
          </button>
        </div>

        <div v-if="alert.show" :class="['auth-alert', `alert-${alert.type}`]" style="margin-bottom:16px">
          <span>{{ alert.message }}</span>
        </div>

        <div v-if="loading" style="text-align:center;padding:40px;color:#64748b">Chargement…</div>

        <div v-else-if="medecins.length === 0" class="empty-state" style="margin-top:40px">
          <svg width="44" height="44" fill="none" stroke="#cbd5e1" stroke-width="1.5" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z"/></svg>
          <p>Aucun médecin. Commencez par en créer un.</p>
        </div>

        <template v-else>
          <!-- Barre de recherche + filtre par domaine -->
          <div style="display:flex;flex-wrap:wrap;gap:12px;align-items:center;margin-bottom:16px">
            <div style="position:relative;flex:1;min-width:220px;max-width:340px">
              <svg style="position:absolute;left:10px;top:50%;transform:translateY(-50%);color:#94a3b8" width="15" height="15" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24"><circle cx="11" cy="11" r="8"/><path stroke-linecap="round" stroke-linejoin="round" d="M21 21l-4.35-4.35"/></svg>
              <input v-model="medecinSearch" placeholder="Rechercher un médecin…"
                style="width:100%;padding:8px 12px 8px 32px;border:1.5px solid #e2e8f0;border-radius:8px;font-size:13px;outline:none;box-sizing:border-box"/>
            </div>
          </div>

          <div style="display:flex;flex-wrap:wrap;gap:8px;margin-bottom:20px">
            <button v-for="pill in medecinDomainPills" :key="pill.key"
              @click="medecinsDomainFilter = pill.key"
              :style="`padding:6px 14px;border-radius:20px;font-size:12px;font-weight:600;cursor:pointer;border:1.5px solid;transition:all .15s;` +
                (medecinsDomainFilter === pill.key
                  ? 'background:#2563eb;color:#fff;border-color:#2563eb'
                  : 'background:#fff;color:#475569;border-color:#e2e8f0')">
              <span v-if="pill.emoji">{{ pill.emoji }} </span>{{ pill.label }}
              <span style="margin-left:5px;opacity:.7">({{ pill.count }})</span>
            </button>
          </div>

          <!-- Sections groupées par domaine -->
          <div v-if="filteredMedecinGroups.length === 0" class="empty-state">
            <p>Aucun médecin pour cette spécialité.</p>
          </div>

          <div v-for="group in filteredMedecinGroups" :key="group.nomDomaine" style="margin-bottom:32px">
            <!-- Header du groupe -->
            <div style="display:flex;align-items:center;gap:10px;margin-bottom:12px;padding-bottom:8px;border-bottom:2px solid #e2e8f0">
              <span style="font-size:22px">{{ group.emoji }}</span>
              <div>
                <h4 style="margin:0;font-size:16px;font-weight:700;color:#1e293b">{{ group.nomDomaine }}</h4>
                <span style="font-size:12px;color:#64748b">{{ group.doctors.length }} médecin{{ group.doctors.length > 1 ? 's' : '' }}</span>
              </div>
              <div style="margin-left:auto">
                <span style="background:#eff6ff;color:#2563eb;padding:3px 10px;border-radius:12px;font-size:12px;font-weight:600">
                  {{ group.doctors.length }}
                </span>
              </div>
            </div>

            <!-- Tableau des médecins du groupe -->
            <div class="admin-table-wrap">
              <table class="admin-table">
                <thead>
                  <tr>
                    <th>Médecin</th>
                    <th>Établissement</th>
                    <th>Secteur</th>
                    <th>Ville</th>
                    <th>N° Ordre</th>
                    <th>Tarif</th>
                    <th>Statut</th>
                    <th></th>
                  </tr>
                </thead>
                <tbody>
                  <tr v-for="m in group.doctors" :key="m.idMedecin">
                    <td>
                      <div style="display:flex;align-items:center;gap:10px">
                        <div class="table-avatar" :style="`background:${avatarColor(m.nom)}`">{{ initials(m.nom, m.prenom) }}</div>
                        <div>
                          <p style="font-weight:600;color:#1e293b;margin:0">Dr. {{ m.prenom }} {{ m.nom }}</p>
                          <p style="font-size:12px;color:#64748b;margin:0">{{ m.email }}</p>
                        </div>
                      </div>
                    </td>
                    <td style="color:#475569;font-size:13px">{{ m.nomEtablissement || '—' }}</td>
                    <td style="color:#475569;font-size:13px">{{ m.nomSecteur || '—' }}</td>
                    <td style="color:#475569;font-size:13px">{{ m.nomVille || '—' }}</td>
                    <td style="color:#475569;font-size:13px;font-family:monospace">{{ m.numeroOrdre }}</td>
                    <td style="font-weight:600;color:#2563eb">{{ m.tarifConsultation ? m.tarifConsultation + ' DH' : '—' }}</td>
                    <td><span :class="['status-pill', m.actif ? 'pill-active' : 'pill-inactive']">{{ m.actif ? 'Actif' : 'Inactif' }}</span></td>
                    <td>
                      <button class="btn-delete" @click="deleteMedecin(m)" title="Supprimer">
                        <svg width="15" height="15" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16"/></svg>
                      </button>
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>
        </template>
      </div>

      <!-- ══ PAGE: Réceptionnistes ══ -->
      <div v-else-if="activeNav === 'receptionnistes'" class="page-content">
        <div class="page-header-row">
          <div>
            <h3 style="margin:0;font-size:20px;font-weight:700;color:#1e293b">Réceptionnistes</h3>
            <p style="margin:4px 0 0;font-size:13px;color:#64748b">{{ receptionnistes.length }} réceptionniste(s) — {{ recepByEtablissement.length }} établissement(s)</p>
          </div>
          <button class="btn-primary" style="width:auto;padding:10px 20px" @click="openRecepModal">
            + Ajouter une réceptionniste
          </button>
        </div>

        <div v-if="alert.show" :class="['auth-alert', `alert-${alert.type}`]" style="margin-bottom:16px">
          <span>{{ alert.message }}</span>
        </div>

        <div v-if="loading" style="text-align:center;padding:40px;color:#64748b">Chargement…</div>

        <div v-else-if="receptionnistes.length === 0" class="empty-state" style="margin-top:40px">
          <svg width="44" height="44" fill="none" stroke="#cbd5e1" stroke-width="1.5" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" d="M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0"/></svg>
          <p>Aucune réceptionniste. Commencez par en créer une.</p>
        </div>

        <template v-else>
          <!-- Barre de recherche + filtre par établissement -->
          <div style="display:flex;flex-wrap:wrap;gap:12px;align-items:center;margin-bottom:16px">
            <div style="position:relative;flex:1;min-width:220px;max-width:340px">
              <svg style="position:absolute;left:10px;top:50%;transform:translateY(-50%);color:#94a3b8" width="15" height="15" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24"><circle cx="11" cy="11" r="8"/><path stroke-linecap="round" stroke-linejoin="round" d="M21 21l-4.35-4.35"/></svg>
              <input v-model="recepSearch" placeholder="Rechercher une réceptionniste…"
                style="width:100%;padding:8px 12px 8px 32px;border:1.5px solid #e2e8f0;border-radius:8px;font-size:13px;outline:none;box-sizing:border-box"/>
            </div>
          </div>

          <div style="display:flex;flex-wrap:wrap;gap:8px;margin-bottom:20px">
            <button v-for="pill in recepEtabPills" :key="pill.key"
              @click="recepEtabFilter = pill.key"
              :style="`padding:6px 14px;border-radius:20px;font-size:12px;font-weight:600;cursor:pointer;border:1.5px solid;transition:all .15s;` +
                (recepEtabFilter === pill.key
                  ? 'background:#2563eb;color:#fff;border-color:#2563eb'
                  : 'background:#fff;color:#475569;border-color:#e2e8f0')">
              🏥 {{ pill.label }}
              <span style="margin-left:5px;opacity:.7">({{ pill.count }})</span>
            </button>
          </div>

          <!-- Sections groupées par établissement -->
          <div v-if="filteredRecepGroups.length === 0" class="empty-state">
            <p>Aucune réceptionniste pour cet établissement.</p>
          </div>

          <div v-for="group in filteredRecepGroups" :key="group.nomEtablissement" style="margin-bottom:32px">
            <!-- Header du groupe -->
            <div style="display:flex;align-items:center;gap:10px;margin-bottom:12px;padding-bottom:8px;border-bottom:2px solid #e2e8f0">
              <span style="font-size:22px">🏥</span>
              <div>
                <h4 style="margin:0;font-size:16px;font-weight:700;color:#1e293b">{{ group.nomEtablissement }}</h4>
                <span style="font-size:12px;color:#64748b">
                  {{ group.staff.length }} réceptionniste{{ group.staff.length > 1 ? 's' : '' }}
                  <span v-if="group.nomSecteur"> · {{ group.nomSecteur }}</span>
                  <span v-if="group.nomVille"> · {{ group.nomVille }}</span>
                </span>
              </div>
              <div style="margin-left:auto">
                <span style="background:#eff6ff;color:#2563eb;padding:3px 10px;border-radius:12px;font-size:12px;font-weight:600">
                  {{ group.staff.length }}
                </span>
              </div>
            </div>

            <!-- Tableau des réceptionnistes du groupe -->
            <div class="admin-table-wrap">
              <table class="admin-table">
                <thead>
                  <tr>
                    <th>Réceptionniste</th>
                    <th>Secteur</th>
                    <th>Ville</th>
                    <th>Téléphone</th>
                    <th></th>
                  </tr>
                </thead>
                <tbody>
                  <tr v-for="r in group.staff" :key="r.idReceptionniste">
                    <td>
                      <div style="display:flex;align-items:center;gap:10px">
                        <div class="table-avatar" :style="`background:${avatarColor(r.nom)}`">{{ initials(r.nom, r.prenom) }}</div>
                        <div>
                          <p style="font-weight:600;color:#1e293b;margin:0">{{ r.prenom }} {{ r.nom }}</p>
                          <p style="font-size:12px;color:#64748b;margin:0">{{ r.email }}</p>
                        </div>
                      </div>
                    </td>
                    <td style="color:#475569;font-size:13px">{{ r.nomSecteur || '—' }}</td>
                    <td style="color:#475569;font-size:13px">{{ r.nomVille || '—' }}</td>
                    <td style="color:#475569;font-size:13px">{{ r.telephone || '—' }}</td>
                    <td>
                      <button class="btn-delete" @click="deleteReceptionniste(r)" title="Supprimer">
                        <svg width="15" height="15" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16"/></svg>
                      </button>
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>
        </template>
      </div>

    </main>

    <!-- ═══ WIZARD: Créer médecin / réceptionniste ═══ -->
    <div v-if="wizard.show" class="modal-overlay" @click.self="closeWizard">
      <div class="wizard-box">

        <!-- Header -->
        <div class="modal-header">
          <h3>{{ wizard.mode === 'medecin' ? '👨‍⚕️ Créer un compte médecin' : '🧑‍💼 Créer un compte réceptionniste' }}</h3>
          <button class="modal-close" @click="closeWizard">✕</button>
        </div>

        <!-- Stepper -->
        <div class="wz-stepper">
          <div v-for="(label, i) in wizardStepLabels" :key="i"
            :class="['wz-step', { active: wizard.step === i+1, done: wizard.step > i+1 }]">
            <div class="wz-num">
              <svg v-if="wizard.step > i+1" width="14" height="14" fill="none" stroke="currentColor" stroke-width="2.5" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" d="M5 13l4 4L19 7"/></svg>
              <span v-else>{{ i+1 }}</span>
            </div>
            <span class="wz-label">{{ label }}</span>
            <div v-if="i < wizardStepLabels.length - 1" class="wz-connector" :class="{ filled: wizard.step > i+1 }"></div>
          </div>
        </div>

        <!-- ── Step 1 (médecin) : Domaine ── -->
        <div v-if="wizard.mode === 'medecin' && wizard.step === 1" class="wizard-body wz-domain-body">
          <!-- Search -->
          <div class="wz-search-bar" style="margin-bottom:12px">
            <svg width="16" height="16" fill="none" stroke="#94a3b8" stroke-width="2" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"/></svg>
            <input v-model="domaineSearch" placeholder="Cardiologie, Dentaire, Pédiatrie..."/>
          </div>
          <!-- Category pills -->
          <div class="wz-cat-pills">
            <button v-for="c in domaineCategories" :key="c.key"
              :class="['wz-cat-pill', { active: activeCategorie === c.key }]"
              @click="activeCategorie = c.key">{{ c.label }}</button>
          </div>
          <!-- Cards -->
          <div class="wz-spec-grid">
            <button v-for="d in filteredDomaines" :key="d.idDomaine"
              :class="['wz-spec-card', { selected: wizard.selectedDomaine?.idDomaine === d.idDomaine }]"
              @click="selectDomaine(d)">
              <div class="wz-spec-icon-wrap" :style="`background:${getDomainStyle(d).bg}`">
                <span class="wz-spec-emoji">{{ domaineIcone(d) }}</span>
              </div>
              <p class="wz-spec-name">{{ d.nomDomaine }}</p>
              <span class="wz-spec-badge" :style="`background:${getDomainStyle(d).badge};color:${getDomainStyle(d).badgeText}`">
                {{ getDomainCatLabel(d) }}
              </span>
              <span class="wz-spec-action">Sélectionner →</span>
            </button>
          </div>
          <div v-if="filteredDomaines.length === 0" class="wz-empty">Aucune spécialité trouvée.</div>
        </div>

        <!-- ── Step 2 (médecin) / Step 1 (réceptionniste) : Ville ── -->
        <div v-if="wizard.step === villeStep" class="wizard-body wz-ville-body">
          <p class="wz-hint">Dans quelle <strong>ville</strong> se trouve le cabinet ?</p>
          <div class="wz-search-bar" style="margin-bottom:12px">
            <svg width="16" height="16" fill="none" stroke="#94a3b8" stroke-width="2" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"/></svg>
            <input v-model="villeSearch" placeholder="Rechercher une ville…"/>
          </div>
          <div class="wz-cat-pills" style="margin-bottom:14px">
            <button v-for="r in villeRegions" :key="r"
              :class="['wz-cat-pill', { active: villeRegionFilter === r }]"
              @click="villeRegionFilter = r">{{ r }}</button>
          </div>
          <div class="wz-ville-grid">
            <button v-for="v in filteredVilles" :key="v.idVille"
              :class="['wz-ville-card', { selected: wizard.selectedVille?.idVille === v.idVille }]"
              @click="selectVille(v)">
              <span class="wz-ville-flag">{{ villeFlag(v.nomVille) }}</span>
              <span class="wz-ville-name">{{ v.nomVille }}</span>
              <span class="wz-ville-region">{{ villeRegion(v.nomVille) }}</span>
            </button>
          </div>
          <div v-if="filteredVilles.length === 0" class="wz-empty">Aucune ville trouvée.</div>
        </div>

        <!-- ── Step 3 (médecin) / Step 2 (réceptionniste) : Secteur ── -->
        <div v-if="wizard.step === secteurStep" class="wizard-body">
          <p class="wz-hint">Quel <strong>secteur</strong> à {{ wizard.selectedVille?.nomVille }} ?</p>
          <div v-if="wizard.secteurs.length === 0" class="wz-empty">Aucun secteur disponible dans cette ville.</div>
          <div class="wz-list">
            <button v-for="s in wizard.secteurs" :key="s.idSecteur"
              :class="['wz-list-item', { selected: wizard.selectedSecteur?.idSecteur === s.idSecteur }]"
              @click="selectSecteur(s)">
              <span class="wz-list-emoji">📍</span>
              <span class="wz-list-name">{{ s.nomSecteur }}</span>
              <svg v-if="wizard.selectedSecteur?.idSecteur === s.idSecteur" width="16" height="16" fill="none" stroke="#2563eb" stroke-width="2.5" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" d="M5 13l4 4L19 7"/></svg>
            </button>
          </div>
        </div>

        <!-- ── Step 4 (médecin) / Step 3 (réceptionniste) : Cabinet ── -->
        <div v-if="wizard.step === cabinetStep" class="wizard-body">
          <p class="wz-hint">Quel <strong>cabinet</strong> au secteur {{ wizard.selectedSecteur?.nomSecteur }} ?</p>

          <!-- Liste des cabinets existants -->
          <div class="wz-list" v-if="wizard.cabinets.length > 0 && !showNewCabinetForm">
            <button v-for="c in wizard.cabinets" :key="c.idEtablissement"
              :class="['wz-list-item wz-cabinet-item', { selected: wizard.selectedCabinet?.idEtablissement === c.idEtablissement }]"
              @click="selectCabinet(c)">
              <span class="wz-list-emoji">🏥</span>
              <div class="wz-cabinet-info">
                <p class="wz-cabinet-name">{{ c.nomEtablissement }}</p>
                <p class="wz-cabinet-addr">{{ c.adresse || c.nomSecteur }}</p>
              </div>
              <svg v-if="wizard.selectedCabinet?.idEtablissement === c.idEtablissement" width="16" height="16" fill="none" stroke="#2563eb" stroke-width="2.5" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" d="M5 13l4 4L19 7"/></svg>
            </button>
          </div>

          <!-- Bouton créer (toujours visible si pas de formulaire ouvert) -->
          <button v-if="!showNewCabinetForm" class="wz-add-cabinet-btn" @click="showNewCabinetForm = true">
            <svg width="16" height="16" fill="none" stroke="currentColor" stroke-width="2.5" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" d="M12 4v16m8-8H4"/></svg>
            {{ wizard.cabinets.length === 0 ? 'Créer un nouveau cabinet' : 'Ajouter un autre cabinet' }}
          </button>

          <!-- Formulaire nouveau cabinet -->
          <div v-if="showNewCabinetForm" class="wz-new-cabinet">
            <div class="wz-new-cabinet-header">
              <span>🏥</span>
              <div>
                <p class="wz-section-title">Nouveau cabinet</p>
                <p class="wz-section-sub">{{ wizard.selectedSecteur?.nomSecteur }} · {{ wizard.selectedVille?.nomVille }}</p>
              </div>
            </div>
            <div v-if="cabinetAlert.show" :class="['auth-alert', `alert-${cabinetAlert.type}`]" style="margin-bottom:10px">
              <span>{{ cabinetAlert.message }}</span>
            </div>
            <div class="form-group"><label>Nom du cabinet *</label><input v-model="newCabinetForm.nomEtablissement" type="text" required placeholder="Cabinet Dr. Alaoui"/></div>
            <div class="form-group">
              <label>Type</label>
              <select v-model.number="newCabinetForm.idType" class="form-select">
                <option :value="1">Cabinet</option>
                <option :value="2">Clinique</option>
                <option :value="3">Hôpital</option>
                <option :value="5">Centre de santé</option>
              </select>
            </div>
            <div class="form-group"><label>Adresse</label><input v-model="newCabinetForm.adresse" type="text" placeholder="12 Rue Ibn Sina"/></div>
            <div class="form-group"><label>Téléphone</label><input v-model="newCabinetForm.telephone" type="text" placeholder="0537001122"/></div>
            <div class="wz-new-cabinet-footer">
              <button type="button" class="btn-back" @click="showNewCabinetForm = false; cabinetAlert.show = false">Annuler</button>
              <button type="button" class="btn-primary" style="width:auto;padding:9px 20px" :disabled="creatingCabinet || !newCabinetForm.nomEtablissement" @click="submitNewCabinet">
                {{ creatingCabinet ? 'Création…' : 'Créer et sélectionner' }}
              </button>
            </div>
          </div>
        </div>

        <!-- ── Step 5 (médecin) / Step 4 (réceptionniste) : Formulaire ── -->
        <div v-if="wizard.step === formStep" class="wizard-body">
          <div class="wz-recap">
            <span class="wz-recap-icon">{{ wizard.mode === 'medecin' ? domaineIcone(wizard.selectedDomaine || {}) : '🏥' }}</span>
            <div>
              <p class="wz-recap-name">
                <span v-if="wizard.mode === 'medecin'">{{ wizard.selectedDomaine?.nomDomaine }} — </span>{{ wizard.selectedCabinet?.nomEtablissement }}
              </p>
              <p class="wz-recap-sub">{{ wizard.selectedSecteur?.nomSecteur }} · {{ wizard.selectedVille?.nomVille }}</p>
            </div>
          </div>

          <div v-if="formAlert.show" :class="['auth-alert', `alert-${formAlert.type}`]" style="margin-bottom:12px">
            <span>{{ formAlert.message }}</span>
          </div>

          <!-- Formulaire Médecin + Réceptionniste -->
          <form v-if="wizard.mode === 'medecin'" @submit.prevent="submitMedecin" class="modal-form" style="padding:0">

            <div class="wz-section-header">
              <span class="wz-section-icon">👨‍⚕️</span>
              <div><p class="wz-section-title">Compte Médecin</p><p class="wz-section-sub">Informations du médecin</p></div>
            </div>
            <div class="form-row-2">
              <div class="form-group"><label>Prénom</label><input v-model="medecinForm.prenom" type="text" required placeholder="Karim"/></div>
              <div class="form-group"><label>Nom</label><input v-model="medecinForm.nom" type="text" required placeholder="Alaoui"/></div>
            </div>
            <div class="form-group"><label>Email</label><input v-model="medecinForm.email" type="email" required placeholder="dr.alaoui@exemple.ma"/></div>
            <div class="form-group"><label>Mot de passe</label><input v-model="medecinForm.motDePasse" type="password" required placeholder="Minimum 6 caractères"/></div>
            <div class="form-row-2">
              <div class="form-group"><label>Téléphone</label><input v-model="medecinForm.telephone" type="text" placeholder="0612345678"/></div>
              <div class="form-group"><label>N° Ordre</label><input v-model="medecinForm.numeroOrdre" type="text" required placeholder="ORD-2024-001"/></div>
            </div>
            <div class="form-row-2">
              <div class="form-group"><label>Expérience (ans)</label><input v-model.number="medecinForm.experienceAns" type="number" min="0" placeholder="5"/></div>
              <div class="form-group"><label>Tarif consultation (DH)</label><input v-model.number="medecinForm.tarifConsultation" type="number" min="0" placeholder="300"/></div>
            </div>

            <div class="wz-section-divider">
              <div class="wz-divider-line"></div>
              <span class="wz-divider-label">Réceptionniste du même cabinet</span>
              <div class="wz-divider-line"></div>
            </div>

            <div class="wz-section-header">
              <span class="wz-section-icon">🧑‍💼</span>
              <div><p class="wz-section-title">Compte Réceptionniste</p><p class="wz-section-sub">Créé automatiquement avec le médecin</p></div>
            </div>
            <div class="form-row-2">
              <div class="form-group"><label>Prénom</label><input v-model="linkedRecepForm.prenom" type="text" required placeholder="Sara"/></div>
              <div class="form-group"><label>Nom</label><input v-model="linkedRecepForm.nom" type="text" required placeholder="Benali"/></div>
            </div>
            <div class="form-group"><label>Email</label><input v-model="linkedRecepForm.email" type="email" required placeholder="sara.benali@exemple.ma"/></div>
            <div class="form-group"><label>Mot de passe</label><input v-model="linkedRecepForm.motDePasse" type="password" required placeholder="Minimum 6 caractères"/></div>
            <div class="form-group"><label>Téléphone</label><input v-model="linkedRecepForm.telephone" type="text" placeholder="0661001122"/></div>

            <div class="modal-footer">
              <button type="button" class="btn-back" @click="wizard.step = cabinetStep">← Retour</button>
              <button type="submit" class="btn-primary" style="width:auto;padding:10px 24px" :disabled="submitting">
                {{ submitting ? 'Création…' : 'Créer Médecin + Réceptionniste' }}
              </button>
            </div>
          </form>

          <!-- Formulaire Réceptionniste -->
          <form v-else @submit.prevent="submitRecep" class="modal-form" style="padding:0">
            <div class="form-row-2">
              <div class="form-group"><label>Prénom</label><input v-model="recepForm.prenom" type="text" required placeholder="Sara"/></div>
              <div class="form-group"><label>Nom</label><input v-model="recepForm.nom" type="text" required placeholder="Benali"/></div>
            </div>
            <div class="form-group"><label>Email</label><input v-model="recepForm.email" type="email" required placeholder="sara.benali@exemple.ma"/></div>
            <div class="form-group"><label>Mot de passe</label><input v-model="recepForm.motDePasse" type="password" required placeholder="Minimum 6 caractères"/></div>
            <div class="form-group"><label>Téléphone</label><input v-model="recepForm.telephone" type="text" placeholder="0661001122"/></div>
            <div class="modal-footer">
              <button type="button" class="btn-back" @click="wizard.step = cabinetStep">← Retour</button>
              <button type="submit" class="btn-primary" style="width:auto;padding:10px 24px" :disabled="submitting">
                {{ submitting ? 'Création…' : 'Créer le compte réceptionniste' }}
              </button>
            </div>
          </form>
        </div>

        <!-- Footer navigation steps 1-3 -->
        <div v-if="wizard.step < formStep" class="wizard-footer">
          <button class="btn-back" @click="wizard.step > 1 ? wizard.step-- : closeWizard()">
            {{ wizard.step === 1 ? 'Annuler' : '← Retour' }}
          </button>
        </div>

      </div>
    </div>

  </div>

  <!-- ═══ MODALE CONFIRMATION SUPPRESSION ═══ -->
  <div v-if="confirmModal.show" class="modal-overlay" @click.self="confirmModal.show = false">
    <div class="confirm-box">
      <div class="confirm-icon-wrap">
        <svg width="28" height="28" fill="none" stroke="#ef4444" stroke-width="2" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16"/>
        </svg>
      </div>
      <h3 class="confirm-title">Confirmer la suppression</h3>
      <p class="confirm-desc">Vous êtes sur le point de supprimer le compte de</p>
      <p class="confirm-name">{{ confirmModal.name }}</p>
      <p class="confirm-warn">Cette action est définitive et ne peut pas être annulée.</p>
      <div class="confirm-footer">
        <button class="btn-back" @click="confirmModal.show = false">Annuler</button>
        <button class="btn-danger" :disabled="confirmModal.loading" @click="confirmModal.onConfirm">
          <svg v-if="!confirmModal.loading" width="15" height="15" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16"/></svg>
          {{ confirmModal.loading ? 'Suppression…' : 'Supprimer définitivement' }}
        </button>
      </div>
    </div>
  </div>

</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'

const router     = useRouter()
const API        = 'http://localhost:8081/api/admin'
const PUBLIC_API = 'http://localhost:8081/api/public'
const token      = localStorage.getItem('token')
const user       = JSON.parse(localStorage.getItem('user') || '{}')

const userName     = computed(() => user.prenom ? `${user.prenom} ${user.nom || ''}`.trim() : user.email || 'Admin')
const userInitials = computed(() => {
  if (user.prenom && user.nom) return (user.prenom[0] + user.nom[0]).toUpperCase()
  return (user.email?.[0] || 'A').toUpperCase()
})
const todayStr = computed(() => new Date().toLocaleDateString('fr-MA', { weekday: 'long', day: 'numeric', month: 'long', year: 'numeric' }))

function logout() {
  localStorage.removeItem('token')
  localStorage.removeItem('user')
  router.push('/login')
}

// ── Nav ───────────────────────────────────────────────────────
const activeNav   = ref('home')
const sidebarOpen = ref(false)

const navItems = computed(() => [
  { key: 'home',            label: 'Tableau de bord', icon: '<svg width="18" height="18" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" d="M3 12l2-2m0 0l7-7 7 7M5 10v10a1 1 0 001 1h3m10-11l2 2m-2-2v10a1 1 0 01-1 1h-3m-6 0a1 1 0 001-1v-4a1 1 0 011-1h2a1 1 0 011 1v4a1 1 0 001 1m-6 0h6"/></svg>' },
  { key: 'medecins',        label: 'Médecins',        icon: '<svg width="18" height="18" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z"/></svg>', count: medecins.value.length },
  { key: 'receptionnistes', label: 'Réceptionnistes', icon: '<svg width="18" height="18" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" d="M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0"/></svg>', count: receptionnistes.value.length },
])

// ── Data ──────────────────────────────────────────────────────
const medecins        = ref([])
const receptionnistes = ref([])
const etablissements  = ref([])
const domaines        = ref([])
const loading         = ref(false)
const villes          = ref([])

function hdrs() { return { 'Content-Type': 'application/json', Authorization: `Bearer ${token}` } }

async function fetchAll() {
  loading.value = true
  try {
    const [m, r] = await Promise.all([
      fetch(`${API}/medecins`,        { headers: hdrs() }).then(r => r.json()),
      fetch(`${API}/receptionnistes`, { headers: hdrs() }).then(r => r.json()),
    ])
    if (m && m.status === 401) { router.push('/login'); return }
    medecins.value        = Array.isArray(m) ? m : []
    receptionnistes.value = Array.isArray(r) ? r : []
  } catch {
    showAlert('Erreur de connexion au serveur', 'error')
  } finally {
    loading.value = false
  }
}

onMounted(async () => {
  await fetchAll()
  const [d, e, v] = await Promise.all([
    fetch(`${PUBLIC_API}/domaines`).then(r => r.json()).catch(() => []),
    fetch(`${PUBLIC_API}/etablissements`).then(r => r.json()).catch(() => []),
    fetch(`${PUBLIC_API}/villes`).then(r => r.json()).catch(() => []),
  ])
  domaines.value       = Array.isArray(d) ? d : []
  etablissements.value = Array.isArray(e) ? e : []
  villes.value         = Array.isArray(v) ? v : []
})

// ── Helpers ───────────────────────────────────────────────────
const avatarColors = ['#2563eb','#7c3aed','#0891b2','#be185d','#059669','#dc2626','#d97706']
function avatarColor(nom) { return avatarColors[(nom?.charCodeAt(0) || 0) % avatarColors.length] }
function initials(nom, prenom) { return ((prenom?.[0] || '') + (nom?.[0] || '')).toUpperCase() }

const DOMAIN_EMOJI = {
  'Généraliste': '🩺', 'Médecine d\'urgence': '🚑', 'Médecine interne': '🏥',
  'Médecine du travail': '💼', 'Gériatrie': '👴',
  'Cardiologie': '❤️', 'Chirurgie vasculaire': '🫀', 'Angiologie': '🩸',
  'Neurologie': '🧠', 'Neurochirurgie': '🔬', 'Psychiatrie': '🗣️', 'Psychologie': '💭',
  'Pédiatrie': '👶', 'Néonatologie': '🍼', 'Gynécologie': '👩', 'Obstétrique': '🌸',
  'Gastroentérologie': '🫃', 'Hépatologie': '🫀', 'Néphrologie': '🫘', 'Urologie': '💧',
  'Orthopédie': '🦴', 'Rhumatologie': '🦾', 'Traumatologie': '🩹',
  'ORL': '👂', 'Ophtalmologie': '👁️', 'Dermatologie': '🧴',
  'Dentaire': '🦷', 'Orthodontie': '😁', 'Parodontologie': '🦷',
  'Endocrinologie': '⚗️', 'Diabétologie': '💉', 'Nutrition': '🥗',
  'Pneumologie': '🫁', 'Allergologie': '🌺',
  'Oncologie': '🎗️', 'Hématologie': '🩺',
  'Radiologie': '🩻', 'Biologie médicale': '🔬',
}
function domaineIcone(d) { return DOMAIN_EMOJI[d.nomDomaine] || '🏥' }

// ── Domain search & category filter ──────────────────────────
const domaineSearch   = ref('')
const activeCategorie = ref('all')

const DOMAIN_CAT_MAP = {
  'Généraliste':          { key: 'general',   label: 'Médecine générale' },
  'Médecine d\'urgence':  { key: 'general',   label: 'Médecine générale' },
  'Médecine interne':     { key: 'general',   label: 'Médecine générale' },
  'Médecine du travail':  { key: 'general',   label: 'Médecine générale' },
  'Gériatrie':            { key: 'general',   label: 'Médecine générale' },
  'Cardiologie':          { key: 'cardio',    label: 'Cardio & Vasculaire' },
  'Chirurgie vasculaire': { key: 'cardio',    label: 'Cardio & Vasculaire' },
  'Angiologie':           { key: 'cardio',    label: 'Cardio & Vasculaire' },
  'Neurologie':           { key: 'neuro',     label: 'Neurologie & Psy' },
  'Neurochirurgie':       { key: 'neuro',     label: 'Neurologie & Psy' },
  'Psychiatrie':          { key: 'neuro',     label: 'Neurologie & Psy' },
  'Psychologie':          { key: 'neuro',     label: 'Neurologie & Psy' },
  'Pédiatrie':            { key: 'pediatrie', label: 'Pédiatrie & Femme' },
  'Néonatologie':         { key: 'pediatrie', label: 'Pédiatrie & Femme' },
  'Gynécologie':          { key: 'pediatrie', label: 'Pédiatrie & Femme' },
  'Obstétrique':          { key: 'pediatrie', label: 'Pédiatrie & Femme' },
  'Gastroentérologie':    { key: 'digestif',  label: 'Digestif & Rénal' },
  'Hépatologie':          { key: 'digestif',  label: 'Digestif & Rénal' },
  'Néphrologie':          { key: 'digestif',  label: 'Digestif & Rénal' },
  'Urologie':             { key: 'digestif',  label: 'Digestif & Rénal' },
  'Orthopédie':           { key: 'os',        label: 'Os & Articulations' },
  'Rhumatologie':         { key: 'os',        label: 'Os & Articulations' },
  'Traumatologie':        { key: 'os',        label: 'Os & Articulations' },
  'ORL':                  { key: 'orl',       label: 'ORL, Yeux & Peau' },
  'Ophtalmologie':        { key: 'orl',       label: 'ORL, Yeux & Peau' },
  'Dermatologie':         { key: 'orl',       label: 'ORL, Yeux & Peau' },
  'Dentaire':             { key: 'dentaire',  label: 'Dentaire' },
  'Orthodontie':          { key: 'dentaire',  label: 'Dentaire' },
  'Parodontologie':       { key: 'dentaire',  label: 'Dentaire' },
  'Endocrinologie':       { key: 'endocrine', label: 'Endocrine & Métabo' },
  'Diabétologie':         { key: 'endocrine', label: 'Endocrine & Métabo' },
  'Nutrition':            { key: 'endocrine', label: 'Endocrine & Métabo' },
  'Pneumologie':          { key: 'respi',     label: 'Respiratoire' },
  'Allergologie':         { key: 'respi',     label: 'Respiratoire' },
  'Oncologie':            { key: 'onco',      label: 'Oncologie' },
  'Hématologie':          { key: 'onco',      label: 'Oncologie' },
  'Radiologie':           { key: 'imagerie',  label: 'Imagerie & Biologie' },
  'Biologie médicale':    { key: 'imagerie',  label: 'Imagerie & Biologie' },
}

const DOMAIN_STYLE_MAP = {
  general:   { bg: '#f0f9ff', badge: '#bae6fd', badgeText: '#0369a1' },
  cardio:    { bg: '#fff1f2', badge: '#fecdd3', badgeText: '#be123c' },
  neuro:     { bg: '#faf5ff', badge: '#e9d5ff', badgeText: '#7c3aed' },
  pediatrie: { bg: '#fdf4ff', badge: '#f5d0fe', badgeText: '#a21caf' },
  digestif:  { bg: '#fefce8', badge: '#fef08a', badgeText: '#854d0e' },
  os:        { bg: '#fff7ed', badge: '#fed7aa', badgeText: '#c2410c' },
  orl:       { bg: '#f0fdf4', badge: '#bbf7d0', badgeText: '#15803d' },
  dentaire:  { bg: '#ecfdf5', badge: '#a7f3d0', badgeText: '#065f46' },
  endocrine: { bg: '#fff7ed', badge: '#fdba74', badgeText: '#9a3412' },
  respi:     { bg: '#eff6ff', badge: '#93c5fd', badgeText: '#1e40af' },
  onco:      { bg: '#fff1f2', badge: '#fda4af', badgeText: '#881337' },
  imagerie:  { bg: '#f8fafc', badge: '#cbd5e1', badgeText: '#334155' },
  default:   { bg: '#f8fafc', badge: '#e2e8f0', badgeText: '#475569' },
}

function getDomainStyle(d) {
  const cat = DOMAIN_CAT_MAP[d.nomDomaine]
  return DOMAIN_STYLE_MAP[cat?.key] || DOMAIN_STYLE_MAP.default
}
function getDomainCatLabel(d) {
  return DOMAIN_CAT_MAP[d.nomDomaine]?.label || 'Autre'
}

const domaineCategories = computed(() => {
  const seen = new Set()
  const cats = [{ key: 'all', label: 'Toutes' }]
  domaines.value.forEach(d => {
    const c = DOMAIN_CAT_MAP[d.nomDomaine]
    if (c && !seen.has(c.key)) { seen.add(c.key); cats.push({ key: c.key, label: c.label }) }
  })
  return cats
})

const filteredDomaines = computed(() => {
  let list = domaines.value
  if (activeCategorie.value !== 'all') {
    list = list.filter(d => DOMAIN_CAT_MAP[d.nomDomaine]?.key === activeCategorie.value)
  }
  if (domaineSearch.value) {
    const q = domaineSearch.value.toLowerCase()
    list = list.filter(d => d.nomDomaine.toLowerCase().includes(q))
  }
  return list
})

// ── Médecins groupés par domaine ─────────────────────────────
const medecinsDomainFilter = ref('all')
const medecinSearch = ref('')

const medecinsByDomaine = computed(() => {
  let list = medecins.value
  if (medecinSearch.value) {
    const q = medecinSearch.value.toLowerCase()
    list = list.filter(m =>
      m.nom.toLowerCase().includes(q) ||
      m.prenom.toLowerCase().includes(q) ||
      m.email.toLowerCase().includes(q) ||
      (m.nomDomaine || '').toLowerCase().includes(q)
    )
  }
  const groups = {}
  list.forEach(m => {
    const dom = m.nomDomaine || 'Autre'
    if (!groups[dom]) groups[dom] = []
    groups[dom].push(m)
  })
  return Object.entries(groups)
    .map(([nomDomaine, doctors]) => ({
      nomDomaine,
      emoji: DOMAIN_EMOJI[nomDomaine] || '🏥',
      doctors,
    }))
    .sort((a, b) => a.nomDomaine.localeCompare(b.nomDomaine, 'fr'))
})

const filteredMedecinGroups = computed(() => {
  if (medecinsDomainFilter.value === 'all') return medecinsByDomaine.value
  return medecinsByDomaine.value.filter(g => g.nomDomaine === medecinsDomainFilter.value)
})

const medecinDomainPills = computed(() => {
  const pills = [{ key: 'all', label: 'Tous', count: medecins.value.length }]
  medecinsByDomaine.value.forEach(g => {
    pills.push({ key: g.nomDomaine, label: g.nomDomaine, count: g.doctors.length, emoji: g.emoji })
  })
  return pills
})

// ── Réceptionnistes groupées par établissement ───────────────
const recepSearch = ref('')
const recepEtabFilter = ref('all')

const recepByEtablissement = computed(() => {
  let list = receptionnistes.value
  if (recepSearch.value) {
    const q = recepSearch.value.toLowerCase()
    list = list.filter(r =>
      r.nom.toLowerCase().includes(q) ||
      r.prenom.toLowerCase().includes(q) ||
      (r.email || '').toLowerCase().includes(q) ||
      (r.nomEtablissement || '').toLowerCase().includes(q) ||
      (r.nomSecteur || '').toLowerCase().includes(q) ||
      (r.nomVille || '').toLowerCase().includes(q)
    )
  }
  const groups = {}
  list.forEach(r => {
    const etab = r.nomEtablissement || 'Sans établissement'
    if (!groups[etab]) groups[etab] = []
    groups[etab].push(r)
  })
  return Object.entries(groups)
    .map(([nomEtablissement, staff]) => ({
      nomEtablissement,
      nomSecteur: staff[0]?.nomSecteur || null,
      nomVille: staff[0]?.nomVille || null,
      staff,
    }))
    .sort((a, b) => a.nomEtablissement.localeCompare(b.nomEtablissement, 'fr'))
})

const filteredRecepGroups = computed(() => {
  if (recepEtabFilter.value === 'all') return recepByEtablissement.value
  return recepByEtablissement.value.filter(g => g.nomEtablissement === recepEtabFilter.value)
})

const recepEtabPills = computed(() => {
  const pills = [{ key: 'all', label: 'Tous', count: receptionnistes.value.length }]
  recepByEtablissement.value.forEach(g => {
    pills.push({ key: g.nomEtablissement, label: g.nomEtablissement, count: g.staff.length })
  })
  return pills
})

// ── Alertes ───────────────────────────────────────────────────
const alert     = ref({ show: false, type: 'error', message: '' })
const formAlert = ref({ show: false, type: 'error', message: '' })
function showAlert(msg, type = 'error') { alert.value = { show: true, type, message: msg }; setTimeout(() => alert.value.show = false, 4000) }
function showFormAlert(msg, type = 'error') { formAlert.value = { show: true, type, message: msg } }

// ── Wizard ────────────────────────────────────────────────────
const wizard = ref({
  show: false, mode: 'medecin', step: 1,
  selectedDomaine: null, selectedVille: null, selectedSecteur: null, selectedCabinet: null,
  secteurs: [], cabinets: [],
})

const wizardStepLabels = computed(() =>
  wizard.value.mode === 'medecin'
    ? ['Domaine', 'Ville', 'Secteur', 'Cabinet', 'Informations']
    : ['Ville', 'Secteur', 'Cabinet', 'Informations']
)
const villeStep   = computed(() => wizard.value.mode === 'medecin' ? 2 : 1)
const secteurStep = computed(() => wizard.value.mode === 'medecin' ? 3 : 2)
const cabinetStep = computed(() => wizard.value.mode === 'medecin' ? 4 : 3)
const formStep    = computed(() => wizard.value.mode === 'medecin' ? 5 : 4)

const villeSearch       = ref('')
const villeRegionFilter = ref('Toutes')

const VILLE_FLAG_MAP = {
  'Casablanca': '🏙️', 'Mohammedia': '🏭', 'Settat': '🌾', 'Berrechid': '🏘️', 'Benslimane': '🌳', 'El Jadida': '🏰',
  'Rabat': '🏛️', 'Salé': '🕍', 'Témara': '🌆', 'Kénitra': '🌿', 'Khémisset': '🌻', 'Sidi Kacem': '🌵', 'Sidi Slimane': '🌴', 'Souk El Arbaa': '🛒',
  'Fès': '🏺', 'Meknès': '🦁', 'Taza': '⛰️', 'Sefrou': '🍊', 'Ifrane': '❄️', 'Azrou': '🌲', 'Khénifra': '🏔️',
  'Tanger': '⚓', 'Tétouan': '⛵', 'Al Hoceïma': '🌊', 'Chefchaouen': '💙', 'Larache': '🏖️', "M'Diq": '🐟', 'Fnideq': '🛳️', 'Ksar el-Kébir': '🏯',
  'Oujda': '🌄', 'Nador': '⚙️', 'Berkane': '🍋', 'Taourirt': '🏜️', 'Figuig': '🌴', 'Driouch': '🗻',
  'Marrakech': '🕌', 'Safi': '🏺', 'Essaouira': '🎠', 'Ben Guerir': '⚗️', 'Youssoufia': '⛏️', 'Kelaa des Sraghna': '🌹',
  'Agadir': '🏄', 'Tiznit': '🐪', 'Taroudant': '🏰', 'Inezgane': '🏗️', 'Oulad Teima': '🌾',
  'Béni Mellal': '🏞️', 'Khouribga': '⛏️', 'Azilal': '🏔️', 'Fquih Ben Salah': '🌿',
  'Errachidia': '🏜️', 'Ouarzazate': '🎬', 'Zagora': '🐫', 'Midelt': '🍎', 'Tinghir': '🗺️',
  'Guelmim': '🌵', 'Tan-Tan': '🌬️', 'Sidi Ifni': '🌅', 'Tata': '🏔️',
  'Laâyoune': '🌐', 'Tarfaya': '🏝️', 'Boujdour': '🌊',
  'Dakhla': '🏄',
}

const VILLE_REGION_MAP = {
  'Casablanca': 'Casablanca-Settat', 'Mohammedia': 'Casablanca-Settat', 'Settat': 'Casablanca-Settat',
  'Berrechid': 'Casablanca-Settat', 'Benslimane': 'Casablanca-Settat', 'El Jadida': 'Casablanca-Settat',
  'Rabat': 'Rabat-Salé-Kénitra', 'Salé': 'Rabat-Salé-Kénitra', 'Témara': 'Rabat-Salé-Kénitra',
  'Kénitra': 'Rabat-Salé-Kénitra', 'Khémisset': 'Rabat-Salé-Kénitra', 'Sidi Kacem': 'Rabat-Salé-Kénitra',
  'Sidi Slimane': 'Rabat-Salé-Kénitra', 'Souk El Arbaa': 'Rabat-Salé-Kénitra',
  'Fès': 'Fès-Meknès', 'Meknès': 'Fès-Meknès', 'Taza': 'Fès-Meknès',
  'Sefrou': 'Fès-Meknès', 'Ifrane': 'Fès-Meknès', 'Azrou': 'Fès-Meknès', 'Khénifra': 'Fès-Meknès',
  'Tanger': 'Tanger-Tétouan', 'Tétouan': 'Tanger-Tétouan', 'Al Hoceïma': 'Tanger-Tétouan',
  'Chefchaouen': 'Tanger-Tétouan', 'Larache': 'Tanger-Tétouan', "M'Diq": 'Tanger-Tétouan',
  'Fnideq': 'Tanger-Tétouan', 'Ksar el-Kébir': 'Tanger-Tétouan',
  'Oujda': 'Oriental', 'Nador': 'Oriental', 'Berkane': 'Oriental',
  'Taourirt': 'Oriental', 'Figuig': 'Oriental', 'Driouch': 'Oriental',
  'Marrakech': 'Marrakech-Safi', 'Safi': 'Marrakech-Safi', 'Essaouira': 'Marrakech-Safi',
  'Ben Guerir': 'Marrakech-Safi', 'Youssoufia': 'Marrakech-Safi', 'Kelaa des Sraghna': 'Marrakech-Safi',
  'Agadir': 'Souss-Massa', 'Tiznit': 'Souss-Massa', 'Taroudant': 'Souss-Massa',
  'Inezgane': 'Souss-Massa', 'Oulad Teima': 'Souss-Massa',
  'Béni Mellal': 'Béni Mellal-Khénifra', 'Khouribga': 'Béni Mellal-Khénifra',
  'Azilal': 'Béni Mellal-Khénifra', 'Fquih Ben Salah': 'Béni Mellal-Khénifra',
  'Errachidia': 'Drâa-Tafilalet', 'Ouarzazate': 'Drâa-Tafilalet', 'Zagora': 'Drâa-Tafilalet',
  'Midelt': 'Drâa-Tafilalet', 'Tinghir': 'Drâa-Tafilalet',
  'Guelmim': 'Guelmim-Oued Noun', 'Tan-Tan': 'Guelmim-Oued Noun',
  'Sidi Ifni': 'Guelmim-Oued Noun', 'Tata': 'Guelmim-Oued Noun',
  'Laâyoune': 'Laâyoune-Sakia El Hamra', 'Tarfaya': 'Laâyoune-Sakia El Hamra', 'Boujdour': 'Laâyoune-Sakia El Hamra',
  'Dakhla': 'Dakhla-Oued Ed-Dahab',
}

function villeFlag(nom) { return VILLE_FLAG_MAP[nom] || '🏙️' }
function villeRegion(nom) { return VILLE_REGION_MAP[nom] || 'Autre' }

const villeRegions = computed(() => {
  const set = new Set(villes.value.map(v => villeRegion(v.nomVille)))
  return ['Toutes', ...Array.from(set).sort()]
})

const filteredVilles = computed(() => {
  return villes.value.filter(v => {
    const matchRegion = villeRegionFilter.value === 'Toutes' || villeRegion(v.nomVille) === villeRegionFilter.value
    const matchSearch = !villeSearch.value || v.nomVille.toLowerCase().includes(villeSearch.value.toLowerCase())
    return matchRegion && matchSearch
  })
})

async function openWizard(mode) {
  villeSearch.value = ''
  villeRegionFilter.value = 'Toutes'
  domaineSearch.value = ''
  activeCategorie.value = 'all'
  wizard.value = { show: true, mode, step: 1, selectedDomaine: null, selectedVille: null, selectedSecteur: null, selectedCabinet: null, secteurs: [], cabinets: [] }
  formAlert.value.show = false
  showNewCabinetForm.value = false
  newCabinetForm.value = { nomEtablissement: '', idType: 1, adresse: '', telephone: '' }
  cabinetAlert.value.show = false
  if (mode === 'medecin') {
    medecinForm.value = { prenom: '', nom: '', email: '', motDePasse: '', telephone: '', numeroOrdre: '', idDomaine: '', idEtablissement: '', experienceAns: null, tarifConsultation: null }
    linkedRecepForm.value = { prenom: '', nom: '', email: '', motDePasse: '', telephone: '' }
    if (domaines.value.length === 0) {
      const d = await fetch(`${PUBLIC_API}/domaines`).then(r => r.json()).catch(() => [])
      domaines.value = Array.isArray(d) ? d : []
    }
  } else {
    recepForm.value = { prenom: '', nom: '', email: '', motDePasse: '', telephone: '', idEtablissement: '' }
  }
  if (villes.value.length === 0) {
    const v = await fetch(`${PUBLIC_API}/villes`).then(r => r.json()).catch(() => [])
    villes.value = Array.isArray(v) ? v : []
  }
}

function closeWizard() { wizard.value.show = false }
function openMedecinModal()      { openWizard('medecin') }
function openRecepModal()        { openWizard('receptionniste') }

function selectDomaine(d) {
  wizard.value.selectedDomaine = d
  medecinForm.value.idDomaine = d.idDomaine
  wizard.value.step = 2
}

async function selectVille(v) {
  wizard.value.selectedVille = v
  wizard.value.selectedSecteur = null
  wizard.value.selectedCabinet = null
  const s = await fetch(`${PUBLIC_API}/secteurs?idVille=${v.idVille}`).then(r => r.json()).catch(() => [])
  wizard.value.secteurs = Array.isArray(s) ? s : []
  wizard.value.step = secteurStep.value
}

async function selectSecteur(s) {
  wizard.value.selectedSecteur = s
  wizard.value.selectedCabinet = null
  const c = await fetch(`${PUBLIC_API}/etablissements?idSecteur=${s.idSecteur}`).then(r => r.json()).catch(() => [])
  wizard.value.cabinets = Array.isArray(c) ? c : []
  wizard.value.step = cabinetStep.value
}

function selectCabinet(c) {
  wizard.value.selectedCabinet = c
  if (wizard.value.mode === 'medecin') medecinForm.value.idEtablissement = c.idEtablissement
  else recepForm.value.idEtablissement = c.idEtablissement
  wizard.value.step = formStep.value
}

// ── Formulaires ───────────────────────────────────────────────
const submitting  = ref(false)
const medecinForm     = ref({ prenom: '', nom: '', email: '', motDePasse: '', telephone: '', numeroOrdre: '', idDomaine: '', idEtablissement: '', experienceAns: null, tarifConsultation: null })
const recepForm       = ref({ prenom: '', nom: '', email: '', motDePasse: '', telephone: '', idEtablissement: '' })
const linkedRecepForm = ref({ prenom: '', nom: '', email: '', motDePasse: '', telephone: '' })

// ── Nouveau cabinet inline ─────────────────────────────────────
const showNewCabinetForm = ref(false)
const creatingCabinet    = ref(false)
const newCabinetForm     = ref({ nomEtablissement: '', idType: 1, adresse: '', telephone: '' })
const cabinetAlert       = ref({ show: false, type: 'error', message: '' })

async function submitNewCabinet() {
  if (!newCabinetForm.value.nomEtablissement.trim()) return
  creatingCabinet.value = true
  cabinetAlert.value.show = false
  try {
    const payload = { ...newCabinetForm.value, idSecteur: wizard.value.selectedSecteur?.idSecteur }
    const res = await fetch(`${API}/etablissements`, { method: 'POST', headers: hdrs(), body: JSON.stringify(payload) })
    if (res.ok) {
      const created = await res.json()
      wizard.value.cabinets.push(created)
      showNewCabinetForm.value = false
      newCabinetForm.value = { nomEtablissement: '', idType: 1, adresse: '', telephone: '' }
      selectCabinet(created)
    } else {
      const err = await res.json().catch(() => ({}))
      cabinetAlert.value = { show: true, type: 'error', message: err.message || 'Erreur lors de la création' }
    }
  } catch { cabinetAlert.value = { show: true, type: 'error', message: 'Erreur de connexion' } }
  finally { creatingCabinet.value = false }
}

const confirmModal = ref({ show: false, name: '', loading: false, onConfirm: () => {} })

function deleteMedecin(m) {
  confirmModal.value = {
    show: true,
    name: `Dr. ${m.prenom} ${m.nom}`,
    loading: false,
    onConfirm: async () => {
      confirmModal.value.loading = true
      try {
        const res = await fetch(`${API}/medecins/${m.idMedecin}`, { method: 'DELETE', headers: hdrs() })
        if (res.ok) {
          medecins.value = medecins.value.filter(x => x.idMedecin !== m.idMedecin)
          confirmModal.value.show = false
          showAlert('Médecin supprimé avec succès', 'success')
        } else {
          showAlert('Erreur lors de la suppression', 'error')
          confirmModal.value.show = false
        }
      } catch {
        showAlert('Erreur de connexion', 'error')
        confirmModal.value.show = false
      }
    }
  }
}

function deleteReceptionniste(r) {
  confirmModal.value = {
    show: true,
    name: `${r.prenom} ${r.nom}`,
    loading: false,
    onConfirm: async () => {
      confirmModal.value.loading = true
      try {
        const res = await fetch(`${API}/receptionnistes/${r.idReceptionniste}`, { method: 'DELETE', headers: hdrs() })
        if (res.ok) {
          receptionnistes.value = receptionnistes.value.filter(x => x.idReceptionniste !== r.idReceptionniste)
          confirmModal.value.show = false
          showAlert('Réceptionniste supprimée avec succès', 'success')
        } else {
          showAlert('Erreur lors de la suppression', 'error')
          confirmModal.value.show = false
        }
      } catch {
        showAlert('Erreur de connexion', 'error')
        confirmModal.value.show = false
      }
    }
  }
}

async function submitMedecin() {
  submitting.value = true
  formAlert.value.show = false
  try {
    const res = await fetch(`${API}/medecins`, { method: 'POST', headers: hdrs(), body: JSON.stringify(medecinForm.value) })
    if (!res.ok) {
      const err = await res.json().catch(() => ({}))
      showFormAlert(err.message || 'Erreur lors de la création du médecin')
      return
    }
    const recepPayload = { ...linkedRecepForm.value, idEtablissement: medecinForm.value.idEtablissement }
    const res2 = await fetch(`${API}/receptionnistes`, { method: 'POST', headers: hdrs(), body: JSON.stringify(recepPayload) })
    if (!res2.ok) {
      const err2 = await res2.json().catch(() => ({}))
      showFormAlert('Médecin créé mais erreur réceptionniste: ' + (err2.message || 'inconnue'))
      return
    }
    closeWizard()
    await fetchAll()
    showAlert('Médecin et réceptionniste créés avec succès', 'success')
  } catch { showFormAlert('Erreur de connexion au serveur') }
  finally { submitting.value = false }
}

async function submitRecep() {
  submitting.value = true
  formAlert.value.show = false
  try {
    const res = await fetch(`${API}/receptionnistes`, { method: 'POST', headers: hdrs(), body: JSON.stringify(recepForm.value) })
    if (res.ok) {
      closeWizard()
      await fetchAll()
      showAlert('Réceptionniste créée avec succès', 'success')
    } else {
      const err = await res.json().catch(() => ({}))
      showFormAlert(err.message || 'Erreur lors de la création')
    }
  } catch { showFormAlert('Erreur de connexion au serveur') }
  finally { submitting.value = false }
}
</script>

<style scoped>
/* ══ ROOT ══ */
.dash-root { display:flex; min-height:100vh; background:#f1f5f9; font-family:inherit; }

/* ══ SIDEBAR ══ */
.sidebar { width:256px; flex-shrink:0; background:#0f172a; display:flex; flex-direction:column; position:fixed; top:0; left:0; height:100vh; z-index:100; transition:transform .3s; }
@media(max-width:768px){ .sidebar{ transform:translateX(-100%); } .sidebar.sidebar-open{ transform:translateX(0); } }
.sidebar-overlay { display:none; position:fixed; inset:0; background:rgba(0,0,0,.6); z-index:99; backdrop-filter:blur(3px); }
@media(max-width:768px){ .sidebar-overlay{ display:block; } }

.sb-brand { display:flex; align-items:center; gap:12px; padding:24px 20px 16px; }
.sb-logo { width:38px; height:38px; background:linear-gradient(135deg,#2563eb,#0ea5e9); border-radius:10px; display:flex; align-items:center; justify-content:center; flex-shrink:0; }
.sb-appname { font-size:16px; font-weight:800; color:#fff; margin:0; letter-spacing:-.01em; }
.sb-tagline { font-size:10px; color:rgba(255,255,255,.35); margin:1px 0 0; font-weight:500; text-transform:uppercase; letter-spacing:.08em; }
.sb-section-lbl { font-size:10px; font-weight:700; color:rgba(255,255,255,.2); letter-spacing:.12em; padding:14px 20px 6px; text-transform:uppercase; }

.sb-nav { padding:0 10px; display:flex; flex-direction:column; gap:2px; }
.sb-item { display:flex; align-items:center; gap:11px; padding:10px 12px; border-radius:10px; border:none; cursor:pointer; background:transparent; font-size:13px; font-weight:500; color:rgba(255,255,255,.45); text-align:left; transition:all .18s; width:100%; position:relative; overflow:hidden; }
.sb-item::before { content:''; position:absolute; left:0; top:50%; transform:translateY(-50%); width:3px; height:0; background:#3b82f6; border-radius:0 3px 3px 0; transition:height .18s; }
.sb-item:hover { background:rgba(255,255,255,.06); color:rgba(255,255,255,.85); }
.sb-item.active { background:rgba(59,130,246,.18); color:#fff; font-weight:600; }
.sb-item.active::before { height:22px; }
.sb-item-icon { display:flex; flex-shrink:0; }
.sb-item-lbl { flex:1; }
.sb-count { background:#2563eb; color:#fff; font-size:10px; font-weight:800; padding:1px 7px; border-radius:99px; }

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
.user-avatar-top { width:36px; height:36px; background:linear-gradient(135deg,#2563eb,#7c3aed); border-radius:10px; display:flex; align-items:center; justify-content:center; color:#fff; font-weight:800; font-size:13px; cursor:pointer; }

/* ══ PAGE ══ */
.page-content { padding:28px; display:flex; flex-direction:column; gap:22px; }

/* ══ HERO ══ */
.hero-card { background:linear-gradient(135deg,#1d4ed8 0%,#2563eb 45%,#0ea5e9 100%); border-radius:22px; padding:36px 40px; display:flex; align-items:center; gap:24px; overflow:hidden; position:relative; }
.hero-card::before { content:''; position:absolute; top:-50px; right:240px; width:220px; height:220px; border-radius:50%; background:rgba(255,255,255,.05); }
.hero-left { flex:1; min-width:0; position:relative; z-index:1; }
.hero-eyebrow { font-size:11px; font-weight:700; color:rgba(255,255,255,.6); text-transform:uppercase; letter-spacing:.1em; margin:0 0 8px; }
.hero-title { font-size:26px; font-weight:800; color:#fff; margin:0 0 10px; line-height:1.25; }
.hero-sub { font-size:14px; color:rgba(255,255,255,.75); margin:0; max-width:400px; line-height:1.6; }

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

/* ══ QUICK ACTIONS ══ */
.quick-list { display:flex; flex-direction:column; gap:8px; }
.quick-item { display:flex; align-items:center; gap:14px; padding:13px 14px; border-radius:13px; border:1.5px solid #f1f5f9; background:#fff; cursor:pointer; transition:all .18s; text-align:left; }
.quick-item:hover { border-color:#dbeafe; background:#fafbff; transform:translateX(3px); }
.qi-icon { width:44px; height:44px; border-radius:11px; display:flex; align-items:center; justify-content:center; flex-shrink:0; }
.qi-title { font-size:13px; font-weight:700; color:#0f172a; margin:0 0 2px; }
.qi-sub { font-size:11px; color:#94a3b8; margin:0; }

/* ══ HISTORIQUE ══ */
.hist-row { display:flex; align-items:center; gap:14px; padding:12px 0; border-bottom:1px solid #f8fafc; }
.hist-row:last-child { border-bottom:none; }
.hist-avatar { width:40px; height:40px; border-radius:10px; display:flex; align-items:center; justify-content:center; color:#fff; font-weight:700; font-size:14px; flex-shrink:0; }
.hist-info { flex:1; }
.hist-doc { font-size:13px; font-weight:600; color:#0f172a; margin:0 0 2px; }
.hist-meta { font-size:12px; color:#94a3b8; margin:0; }
.hist-badge { font-size:11px; font-weight:700; padding:4px 11px; border-radius:99px; white-space:nowrap; }
.hbadge-done   { background:#dcfce7; color:#16a34a; }
.hbadge-cancel { background:#fef2f2; color:#dc2626; }

/* ══ BACK BUTTON ══ */
.btn-back { padding:10px 20px; background:#f1f5f9; border:none; border-radius:12px; font-size:14px; font-weight:600; color:#64748b; cursor:pointer; transition:background .2s; }
.btn-back:hover { background:#e2e8f0; }

/* ══ EMPTY STATE ══ */
.empty-state { display:flex; flex-direction:column; align-items:center; gap:10px; padding:56px 24px; color:#94a3b8; text-align:center; }
.empty-state p { font-size:14px; color:#64748b; margin:0; }

/* ── Page header ── */
.page-header-row { display:flex; align-items:center; justify-content:space-between; flex-wrap:wrap; gap:12px; }

/* ── Table ── */
.admin-table-wrap { background:#fff; border-radius:12px; border:1px solid #e2e8f0; overflow-x:auto; }
.admin-table      { width:100%; border-collapse:collapse; font-size:13px; }
.admin-table th   { padding:12px 16px; text-align:left; font-size:11px; font-weight:700; text-transform:uppercase; letter-spacing:.05em; color:#64748b; background:#f8fafc; border-bottom:1px solid #e2e8f0; }
.admin-table td   { padding:14px 16px; border-bottom:1px solid #f1f5f9; vertical-align:middle; }
.admin-table tr:last-child td { border-bottom:none; }
.admin-table tr:hover td      { background:#f8fafc; }

.table-avatar { width:34px; height:34px; border-radius:50%; display:flex; align-items:center; justify-content:center; font-size:12px; font-weight:700; color:#fff; flex-shrink:0; }
.spec-badge   { background:#eff6ff; color:#2563eb; font-size:11px; font-weight:600; padding:3px 8px; border-radius:20px; }
.status-pill  { font-size:11px; font-weight:700; padding:3px 10px; border-radius:20px; }
.pill-active  { background:#dcfce7; color:#16a34a; }
.pill-inactive{ background:#fee2e2; color:#dc2626; }

/* ── Modal ── */
.modal-overlay { position:fixed; inset:0; background:rgba(15,23,42,.45); display:flex; align-items:center; justify-content:center; z-index:1000; padding:16px; }
.modal-box     { background:#fff; border-radius:16px; width:100%; max-width:560px; max-height:90vh; overflow-y:auto; box-shadow:0 20px 60px rgba(0,0,0,.2); }
.modal-header  { display:flex; align-items:center; justify-content:space-between; padding:20px 24px 16px; border-bottom:1px solid #f1f5f9; }
.modal-header h3 { margin:0; font-size:17px; font-weight:700; color:#1e293b; }
.modal-close   { background:none; border:none; font-size:18px; color:#94a3b8; cursor:pointer; padding:4px 8px; border-radius:6px; }
.modal-close:hover { background:#f1f5f9; color:#475569; }
.modal-form    { padding:20px 24px; display:flex; flex-direction:column; gap:14px; }
.modal-footer  { display:flex; justify-content:flex-end; gap:10px; padding-top:8px; }

.form-row-2    { display:grid; grid-template-columns:1fr 1fr; gap:14px; }
.form-group    { display:flex; flex-direction:column; gap:5px; }
.form-group label { font-size:12px; font-weight:600; color:#374151; text-transform:uppercase; letter-spacing:.04em; }
.form-group input,
.form-group select { padding:10px 12px; border:1.5px solid #e2e8f0; border-radius:8px; font-size:13px; color:#1e293b; background:#fafafa; outline:none; transition:border .2s; }
.form-group input:focus,
.form-group select:focus { border-color:#2563eb; background:#fff; }

/* ── Alert ── */
.auth-alert   { display:flex; align-items:center; gap:8px; padding:10px 14px; border-radius:8px; font-size:13px; font-weight:500; }
.alert-success{ background:#dcfce7; color:#16a34a; border:1px solid #bbf7d0; }
.alert-error  { background:#fee2e2; color:#dc2626; border:1px solid #fecaca; }

@media (max-width: 640px) {
  .form-row-2 { grid-template-columns: 1fr; }
  .page-header-row { flex-direction:column; align-items:flex-start; }
}

/* ══ WIZARD ══ */
.wizard-box { background:#fff; border-radius:20px; width:100%; max-width:580px; max-height:92vh; overflow-y:auto; box-shadow:0 24px 64px rgba(0,0,0,.22); display:flex; flex-direction:column; }

/* Stepper */
.wz-stepper { display:flex; align-items:flex-start; padding:16px 24px 0; position:relative; }
.wz-step { display:flex; flex-direction:column; align-items:center; gap:5px; flex:1; position:relative; }
.wz-num { width:32px; height:32px; border-radius:50%; background:#e2e8f0; color:#94a3b8; font-size:13px; font-weight:700; display:flex; align-items:center; justify-content:center; transition:all .25s; flex-shrink:0; }
.wz-step.active .wz-num { background:#2563eb; color:#fff; box-shadow:0 0 0 4px rgba(37,99,235,.15); }
.wz-step.done   .wz-num { background:#22c55e; color:#fff; }
.wz-label { font-size:11px; font-weight:600; color:#94a3b8; white-space:nowrap; }
.wz-step.active .wz-label { color:#2563eb; }
.wz-step.done   .wz-label { color:#22c55e; }
.wz-connector { position:absolute; top:16px; left:calc(50% + 16px); right:calc(-50% + 16px); height:2px; background:#e2e8f0; transition:background .25s; }
.wz-connector.filled { background:#22c55e; }

/* Body */
.wizard-body { padding:20px 24px; flex:1; overflow-y:auto; }
.wz-hint { font-size:14px; color:#475569; margin:0 0 16px; }
.wz-hint strong { color:#0f172a; }
.wz-empty { text-align:center; padding:32px; color:#94a3b8; font-size:13px; }

/* Search bar */
.wz-search-bar { display:flex; align-items:center; gap:10px; background:#f8fafc; border:1.5px solid #e2e8f0; border-radius:12px; padding:10px 14px; margin-bottom:16px; transition:border .2s; }
.wz-search-bar:focus-within { border-color:#2563eb; background:#fff; }
.wz-search-bar input { border:none; outline:none; background:transparent; font-size:14px; color:#0f172a; flex:1; }
.wz-search-bar input::placeholder { color:#94a3b8; }

/* City grid */
.wz-city-grid { display:grid; grid-template-columns:repeat(auto-fill, minmax(120px,1fr)); gap:10px; }
.wz-city-card { display:flex; flex-direction:column; align-items:center; gap:8px; padding:16px 10px; background:#fff; border:2px solid #e2e8f0; border-radius:14px; cursor:pointer; transition:all .2s; }
.wz-city-card:hover { border-color:#93c5fd; transform:translateY(-2px); box-shadow:0 6px 16px rgba(37,99,235,.1); }
.wz-city-card.selected { border-color:#2563eb; background:#eff6ff; }
.wz-city-avatar { width:44px; height:44px; border-radius:50%; background:linear-gradient(135deg,#2563eb,#7c3aed); color:#fff; font-size:18px; font-weight:800; display:flex; align-items:center; justify-content:center; }
.wz-city-card span { font-size:13px; font-weight:700; color:#0f172a; }
.wz-city-card.selected span { color:#1d4ed8; }

/* List items (secteurs & cabinets) */
.wz-list { display:flex; flex-direction:column; gap:8px; }
.wz-list-item { display:flex; align-items:center; gap:12px; padding:14px 16px; background:#fff; border:1.5px solid #e2e8f0; border-radius:12px; cursor:pointer; transition:all .18s; text-align:left; width:100%; }
.wz-list-item:hover { border-color:#93c5fd; background:#f8fafc; }
.wz-list-item.selected { border-color:#2563eb; background:#eff6ff; }
.wz-list-emoji { font-size:18px; flex-shrink:0; }
.wz-list-name { flex:1; font-size:14px; font-weight:600; color:#0f172a; }
.wz-cabinet-item { align-items:flex-start; }
.wz-cabinet-info { flex:1; }
.wz-cabinet-name { font-size:14px; font-weight:700; color:#0f172a; margin:0 0 2px; }
.wz-cabinet-addr { font-size:12px; color:#64748b; margin:0; }

/* Recap (step 4) */
.wz-recap { display:flex; align-items:center; gap:12px; background:linear-gradient(135deg,#eff6ff,#e0e7ff); border:1px solid #bfdbfe; border-radius:14px; padding:14px 16px; margin-bottom:16px; }
.wz-recap-icon { font-size:22px; }
.wz-recap-name { font-size:13px; font-weight:700; color:#1e40af; margin:0; }
.wz-recap-sub  { font-size:12px; color:#3b82f6; margin:2px 0 0; }

/* Domain grid */
.wz-domain-grid { display:grid; grid-template-columns:repeat(auto-fill, minmax(120px,1fr)); gap:10px; }
.wz-domain-card { display:flex; flex-direction:column; align-items:center; gap:8px; padding:16px 10px; background:#fff; border:2px solid #e2e8f0; border-radius:14px; cursor:pointer; transition:all .2s; width:100%; }
.wz-domain-card:hover { border-color:#93c5fd; transform:translateY(-2px); box-shadow:0 6px 16px rgba(37,99,235,.1); }
.wz-domain-card.selected { border-color:#2563eb; background:#eff6ff; }
.wz-domain-emoji { font-size:28px; line-height:1; }
.wz-domain-name { font-size:12px; font-weight:700; color:#0f172a; text-align:center; line-height:1.3; }
.wz-domain-card.selected .wz-domain-name { color:#1d4ed8; }

/* Wizard footer */
.wizard-footer { padding:12px 24px 20px; border-top:1px solid #f1f5f9; display:flex; justify-content:flex-start; }

/* ══ DOMAIN STEP ══ */
.wz-domain-body { max-height:65vh; overflow-y:auto; }

/* Category pills */
.wz-cat-pills { display:flex; flex-wrap:wrap; gap:6px; margin-bottom:16px; }
.wz-cat-pill { padding:6px 14px; border-radius:99px; border:1.5px solid #e2e8f0; background:#fff; font-size:12px; font-weight:600; color:#64748b; cursor:pointer; transition:all .18s; white-space:nowrap; }
.wz-cat-pill:hover { border-color:#93c5fd; color:#2563eb; }
.wz-cat-pill.active { background:#2563eb; border-color:#2563eb; color:#fff; }

/* Specialty cards */
.wz-spec-grid { display:grid; grid-template-columns:repeat(auto-fill, minmax(150px,1fr)); gap:12px; }
.wz-spec-card { display:flex; flex-direction:column; align-items:center; background:#fff; border:1.5px solid #f1f5f9; border-radius:16px; cursor:pointer; transition:all .2s; text-align:center; overflow:hidden; padding-bottom:14px; }
.wz-spec-card:hover { border-color:#93c5fd; box-shadow:0 6px 20px rgba(37,99,235,.1); transform:translateY(-2px); }
.wz-spec-card.selected { border-color:#2563eb; box-shadow:0 0 0 3px rgba(37,99,235,.15); }
.wz-spec-icon-wrap { width:100%; padding:20px 0 16px; display:flex; align-items:center; justify-content:center; border-radius:14px 14px 0 0; }
.wz-spec-emoji { font-size:36px; line-height:1; }
.wz-spec-name { font-size:13px; font-weight:700; color:#0f172a; margin:0 0 8px; padding:0 10px; line-height:1.3; width:100%; }
.wz-spec-badge { font-size:10px; font-weight:700; padding:3px 10px; border-radius:99px; margin-bottom:10px; }
.wz-spec-action { font-size:12px; font-weight:600; color:#2563eb; }
.wz-spec-card:hover .wz-spec-action { text-decoration:underline; }
.wz-spec-card.selected .wz-spec-action { color:#1d4ed8; font-weight:700; }

/* ══ FORM SECTIONS (médecin + réceptionniste) ══ */
.wz-section-header { display:flex; align-items:center; gap:12px; margin:4px 0 14px; }
.wz-section-icon { font-size:24px; }
.wz-section-title { font-size:14px; font-weight:700; color:#0f172a; margin:0; }
.wz-section-sub   { font-size:12px; color:#64748b; margin:2px 0 0; }

/* ══ VILLE STEP ══ */
.wz-ville-body { max-height:65vh; overflow-y:auto; }
.wz-ville-grid { display:grid; grid-template-columns:repeat(auto-fill,minmax(130px,1fr)); gap:10px; }
.wz-ville-card { display:flex; flex-direction:column; align-items:center; gap:6px; padding:14px 10px; background:#fff; border:2px solid #e2e8f0; border-radius:14px; cursor:pointer; transition:all .18s; text-align:center; }
.wz-ville-card:hover { border-color:#93c5fd; transform:translateY(-2px); box-shadow:0 6px 16px rgba(37,99,235,.1); }
.wz-ville-card.selected { border-color:#2563eb; background:#eff6ff; }
.wz-ville-flag { font-size:26px; }
.wz-ville-name { font-size:13px; font-weight:700; color:#0f172a; line-height:1.2; }
.wz-ville-card.selected .wz-ville-name { color:#1d4ed8; }
.wz-ville-region { font-size:10px; color:#94a3b8; font-weight:500; }

/* ══ NOUVEAU CABINET INLINE ══ */
/* ══ DELETE BUTTON ══ */
.btn-delete { display:flex; align-items:center; justify-content:center; width:32px; height:32px; border-radius:8px; border:1.5px solid #fecaca; background:#fff5f5; color:#ef4444; cursor:pointer; transition:all .15s; }
.btn-delete:hover { background:#fee2e2; border-color:#f87171; transform:scale(1.08); }

/* ══ CONFIRM MODAL ══ */
.confirm-box { background:#fff; border-radius:20px; padding:32px 28px; width:100%; max-width:420px; box-shadow:0 20px 60px rgba(0,0,0,.18); text-align:center; animation:slideUp .25s ease; }
.confirm-icon-wrap { width:60px; height:60px; background:#fff5f5; border-radius:50%; display:flex; align-items:center; justify-content:center; margin:0 auto 18px; border:2px solid #fecaca; }
.confirm-title { font-size:18px; font-weight:800; color:#1e293b; margin:0 0 8px; }
.confirm-desc  { font-size:14px; color:#64748b; margin:0 0 4px; }
.confirm-name  { font-size:15px; font-weight:700; color:#0f172a; margin:0 0 12px; }
.confirm-warn  { font-size:12px; color:#ef4444; background:#fff5f5; border:1px solid #fecaca; border-radius:8px; padding:8px 12px; margin-bottom:24px; }
.confirm-footer { display:flex; gap:12px; justify-content:center; }
.btn-danger { display:flex; align-items:center; gap:7px; padding:10px 20px; background:#ef4444; color:#fff; border:none; border-radius:10px; font-size:14px; font-weight:700; cursor:pointer; transition:all .15s; }
.btn-danger:hover:not(:disabled) { background:#dc2626; transform:translateY(-1px); }
.btn-danger:disabled { opacity:.6; cursor:not-allowed; }

.wz-add-cabinet-btn { display:flex; align-items:center; gap:8px; width:100%; margin-top:12px; padding:12px 16px; background:#f8fafc; border:2px dashed #cbd5e1; border-radius:12px; color:#2563eb; font-size:13px; font-weight:600; cursor:pointer; transition:all .18s; justify-content:center; }
.wz-add-cabinet-btn:hover { border-color:#2563eb; background:#eff6ff; }
.wz-new-cabinet { background:#f8fafc; border:1.5px solid #e2e8f0; border-radius:14px; padding:16px; margin-top:12px; }
.wz-new-cabinet-header { display:flex; align-items:center; gap:10px; margin-bottom:14px; font-size:22px; }
.wz-new-cabinet-footer { display:flex; justify-content:flex-end; gap:10px; margin-top:14px; }
.form-select { width:100%; padding:10px 12px; border:1.5px solid #e2e8f0; border-radius:10px; font-size:14px; background:#fff; color:#0f172a; outline:none; cursor:pointer; }
.form-select:focus { border-color:#2563eb; box-shadow:0 0 0 3px rgba(37,99,235,.1); }

/* ══ FORM SECTIONS (médecin + réceptionniste) ══ */
.wz-section-divider { display:flex; align-items:center; gap:10px; margin:20px 0 18px; }
.wz-divider-line  { flex:1; height:1px; background:#e2e8f0; }
.wz-divider-label { font-size:11px; font-weight:700; color:#94a3b8; text-transform:uppercase; letter-spacing:.08em; white-space:nowrap; }
</style>
