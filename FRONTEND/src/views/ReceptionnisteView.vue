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
          <p class="sb-tagline">Espace Réceptionniste</p>
        </div>
      </div>

      <p class="sb-section-lbl">NAVIGATION</p>
      <nav class="sb-nav">
        <button v-for="item in navItems" :key="item.key"
          :class="['sb-item', { active: activeNav === item.key }]"
          @click="activeNav = item.key; sidebarOpen = false; if(item.key === 'medecins' && medecins.length === 0) fetchMedecins()">
          <span class="sb-item-icon" v-html="item.icon"></span>
          <span class="sb-item-lbl">{{ item.label }}</span>
          <span v-if="item.key === 'medecins' && medecins.length" class="sb-count">{{ medecins.length }}</span>
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
          <p class="sb-username">{{ userName }}</p>
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
          <div style="background:linear-gradient(135deg,#0891b2 0%,#0e7490 100%);border-radius:16px;padding:28px 32px;color:#fff;margin-bottom:24px;display:flex;align-items:center;gap:20px">
            <div style="width:56px;height:56px;border-radius:50%;background:rgba(255,255,255,.2);display:flex;align-items:center;justify-content:center;font-size:24px;font-weight:700;flex-shrink:0">
              {{ userInitials }}
            </div>
            <div>
              <h2 style="margin:0;font-size:22px;font-weight:700">Bonjour, {{ profil?.prenom || userRaw.prenom }} {{ profil?.nom || userRaw.nom }} 👋</h2>
              <p style="margin:4px 0 0;opacity:.85;font-size:14px">🏥 {{ profil?.nomEtablissement }}</p>
              <p style="margin:2px 0 0;opacity:.7;font-size:13px">{{ profil?.nomSecteur }} · {{ profil?.nomVille }}</p>
            </div>
          </div>

          <!-- Stats -->
          <div class="stats-grid" style="margin-bottom:28px">
            <div class="stat-tile">
              <div class="stat-tile-icon" style="background:#ecfeff">
                <svg width="20" height="20" fill="none" stroke="#0891b2" stroke-width="2" viewBox="0 0 24 24"><rect x="3" y="4" width="18" height="18" rx="2"/><path stroke-linecap="round" d="M16 2v4M8 2v4M3 10h18"/></svg>
              </div>
              <p class="stat-tile-val">{{ rdvStats.today }}</p>
              <p class="stat-tile-lbl">RDV aujourd'hui</p>
            </div>
            <div class="stat-tile">
              <div class="stat-tile-icon" style="background:#f0fdf4">
                <svg width="20" height="20" fill="none" stroke="#16a34a" stroke-width="2" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z"/></svg>
              </div>
              <p class="stat-tile-val">{{ rdvStats.confirmed }}</p>
              <p class="stat-tile-lbl">RDV confirmés</p>
            </div>
            <div class="stat-tile">
              <div class="stat-tile-icon" style="background:#fef3c7">
                <svg width="20" height="20" fill="none" stroke="#d97706" stroke-width="2" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" d="M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0z"/></svg>
              </div>
              <p class="stat-tile-val">{{ medecins.length }}</p>
              <p class="stat-tile-lbl">Médecins du cabinet</p>
            </div>
            <div class="stat-tile">
              <div class="stat-tile-icon" style="background:#fdf4ff">
                <svg width="20" height="20" fill="none" stroke="#9333ea" stroke-width="2" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2"/></svg>
              </div>
              <p class="stat-tile-val">0</p>
              <p class="stat-tile-lbl">Dossiers traités</p>
            </div>
          </div>

          <!-- Médecins du cabinet -->
          <div style="background:#fff;border-radius:12px;border:1.5px solid #e2e8f0;padding:20px 24px">
            <h4 style="margin:0 0 16px;font-size:15px;font-weight:700;color:#1e293b">👨‍⚕️ Médecins du cabinet</h4>
            <div v-if="medecins.length === 0" style="text-align:center;padding:20px;color:#94a3b8;font-size:13px">
              Aucun médecin enregistré dans cet établissement.
            </div>
            <div v-else style="display:grid;gap:12px">
              <div v-for="m in medecins" :key="m.idMedecin"
                style="display:flex;align-items:center;gap:12px;padding:12px;background:#f8fafc;border-radius:10px">
                <div :style="`width:42px;height:42px;border-radius:50%;background:${mColor(m.nom)};display:flex;align-items:center;justify-content:center;font-weight:700;color:#fff;font-size:14px;flex-shrink:0`">
                  {{ mInitials(m) }}
                </div>
                <div style="flex:1;min-width:0">
                  <p style="margin:0;font-weight:600;color:#1e293b;font-size:14px">Dr. {{ m.prenom }} {{ m.nom }}</p>
                  <p style="margin:3px 0 0;font-size:12px">
                    <span style="background:#eff6ff;color:#2563eb;padding:2px 8px;border-radius:12px;font-weight:700">
                      {{ domaineEmoji(m.nomDomaine) }} {{ m.nomDomaine }}
                    </span>
                  </p>
                </div>
                <span style="font-size:12px;font-weight:600;color:#0891b2">{{ m.tarifConsultation ? m.tarifConsultation + ' DH' : '—' }}</span>
              </div>
            </div>
          </div>
        </template>
      </div>

      <!-- PAGE: Gestion RDV -->
      <div v-else-if="activeNav === 'rdv'" class="page-content">
        <div style="display:flex;align-items:center;justify-content:space-between;margin-bottom:20px">
          <h3 style="margin:0;font-size:20px;font-weight:700;color:#1e293b">Gestion des rendez-vous</h3>
          <button class="btn-refresh" @click="fetchRdvs()" :disabled="loadingRdv">
            <svg width="14" height="14" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" d="M4 4v5h5M20 20v-5h-5M4 9a8 8 0 0114.93-3M20 15a8 8 0 01-14.93 3"/></svg>
            Actualiser
          </button>
        </div>

        <div v-if="loadingRdv" style="text-align:center;padding:40px;color:#94a3b8">Chargement…</div>

        <div v-else-if="rdvs.length === 0" class="empty-state" style="margin-top:40px">
          <svg width="48" height="48" fill="none" stroke="#cbd5e1" stroke-width="1.5" viewBox="0 0 24 24"><rect x="3" y="4" width="18" height="18" rx="2"/><path stroke-linecap="round" d="M16 2v4M8 2v4M3 10h18"/></svg>
          <p>Aucun rendez-vous pour votre cabinet.</p>
        </div>

        <div v-else class="rdv-table-wrap">
          <!-- Filtre statut -->
          <div style="margin-bottom:14px;display:flex;gap:8px;flex-wrap:wrap">
            <button v-for="f in rdvFilters" :key="f.key"
              :class="['filter-btn', { active: rdvFilter === f.key }]"
              @click="rdvFilter = f.key">
              {{ f.label }} <span class="filter-count">{{ rdvCount(f.key) }}</span>
            </button>
          </div>

          <div class="rdv-table-scroll">
            <table class="rdv-table">
              <thead>
                <tr>
                  <th>Patient</th>
                  <th>Médecin</th>
                  <th>Spécialité</th>
                  <th>Date / Heure</th>
                  <th>Motif</th>
                  <th>Statut</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="rdv in filteredRdvs" :key="rdv.idRdv">
                  <td>
                    <div class="cell-name">
                      <div class="mini-avatar" :style="{background: pColor(rdv.nomPatient)}">
                        {{ pInitials(rdv.prenomPatient, rdv.nomPatient) }}
                      </div>
                      <div>
                        <div style="font-weight:600;font-size:13px">{{ rdv.prenomPatient }} {{ rdv.nomPatient }}</div>
                      </div>
                    </div>
                  </td>
                  <td>
                    <div class="cell-name">
                      <div class="mini-avatar" :style="{background: pColor(rdv.nomMedecin)}">
                        {{ pInitials(rdv.prenomMedecin, rdv.nomMedecin) }}
                      </div>
                      <div style="font-weight:600;font-size:13px">Dr. {{ rdv.prenomMedecin }} {{ rdv.nomMedecin }}</div>
                    </div>
                  </td>
                  <td><span class="spec-badge">{{ domaineEmoji(rdv.nomDomaine) }} {{ rdv.nomDomaine || '—' }}</span></td>
                  <td style="white-space:nowrap;font-size:13px">{{ rdv.dateHeure }}</td>
                  <td style="font-size:12px;color:#64748b;max-width:120px;overflow:hidden;text-overflow:ellipsis;white-space:nowrap">{{ rdv.motif || '—' }}</td>
                  <td>
                    <span :class="['statut-badge', 'statut-' + (rdv.statut || 'planifie')]">
                      {{ statutLabel(rdv.statut) }}
                    </span>
                  </td>
                  <td>
                    <button class="actions-trigger" @click="openModal(rdv)">
                      Actions <svg width="12" height="12" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24" style="vertical-align:middle"><path stroke-linecap="round" stroke-linejoin="round" d="M19 9l-7 7-7-7"/></svg>
                    </button>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>

      <!-- PAGE: Médecins du cabinet -->
      <div v-else-if="activeNav === 'medecins'" class="page-content">
        <h3 style="margin:0 0 20px;font-size:20px;font-weight:700;color:#1e293b">Médecins du cabinet</h3>

        <div v-if="loadingMedecins" style="text-align:center;padding:40px;color:#64748b">Chargement…</div>
        <div v-else-if="medecins.length === 0" class="empty-state" style="margin-top:40px">
          <svg width="44" height="44" fill="none" stroke="#cbd5e1" stroke-width="1.5" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z"/></svg>
          <p>Aucun médecin dans cet établissement.</p>
        </div>
        <div v-else class="admin-table-wrap">
          <table class="admin-table">
            <thead>
              <tr>
                <th>Médecin</th>
                <th>Spécialité</th>
                <th>N° Ordre</th>
                <th>Tarif</th>
                <th>Statut</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="m in medecins" :key="m.idMedecin">
                <td>
                  <div style="display:flex;align-items:center;gap:10px">
                    <div :style="`width:36px;height:36px;border-radius:50%;background:${mColor(m.nom)};display:flex;align-items:center;justify-content:center;font-weight:700;color:#fff;font-size:12px;flex-shrink:0`">
                      {{ mInitials(m) }}
                    </div>
                    <div>
                      <p style="font-weight:600;color:#1e293b;margin:0;font-size:14px">Dr. {{ m.prenom }} {{ m.nom }}</p>
                      <p style="font-size:12px;color:#64748b;margin:0">{{ m.email }}</p>
                    </div>
                  </div>
                </td>
                <td><span class="spec-badge">{{ domaineEmoji(m.nomDomaine) }} {{ m.nomDomaine }}</span></td>
                <td style="color:#475569;font-size:13px;font-family:monospace">{{ m.numeroOrdre }}</td>
                <td style="font-weight:600;color:#0891b2">{{ m.tarifConsultation ? m.tarifConsultation + ' DH' : '—' }}</td>
                <td><span :class="['status-pill', m.actif ? 'pill-active' : 'pill-inactive']">{{ m.actif ? 'Actif' : 'Inactif' }}</span></td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>

      <!-- PAGE: Mon profil -->
      <div v-else-if="activeNav === 'profil'" class="page-content">
        <div style="max-width:640px">
          <h3 style="margin:0 0 20px;font-size:20px;font-weight:700;color:#1e293b">Mon profil</h3>

          <div style="background:#fff;border-radius:12px;border:1.5px solid #e2e8f0;overflow:hidden">
            <div style="background:linear-gradient(135deg,#0891b2,#0e7490);padding:24px;display:flex;align-items:center;gap:16px">
              <div style="width:64px;height:64px;border-radius:50%;background:rgba(255,255,255,.2);display:flex;align-items:center;justify-content:center;font-size:26px;font-weight:700;color:#fff">
                {{ userInitials }}
              </div>
              <div style="color:#fff">
                <p style="margin:0;font-size:20px;font-weight:700">{{ profil?.prenom || userRaw.prenom }} {{ profil?.nom || userRaw.nom }}</p>
                <p style="margin:4px 0 0;opacity:.85;font-size:14px">Réceptionniste</p>
              </div>
            </div>
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
            </div>
          </div>
        </div>
      </div>

    </main>

    <!-- MODAL Actions RDV -->
    <Teleport to="body">
      <div v-if="rdvModal.show" class="modal-overlay" @click.self="closeModal()">
        <div class="modal-box">
          <div class="modal-header">
            <div>
              <p class="modal-title">{{ rdvModal.rdv?.prenomPatient }} {{ rdvModal.rdv?.nomPatient }}</p>
              <p class="modal-sub">{{ rdvModal.rdv?.dateHeure }} &middot; Dr. {{ rdvModal.rdv?.prenomMedecin }} {{ rdvModal.rdv?.nomMedecin }}</p>
            </div>
            <button class="modal-close" @click="closeModal()">&#x2715;</button>
          </div>

          <!-- Liste des 6 actions -->
          <div v-if="!rdvModal.action" class="modal-actions-grid">
            <button class="mact-btn mact-confirm" @click="quickAction('confirme')">
              <span class="mact-ico">✓</span><span>Confirmer</span>
            </button>
            <button class="mact-btn mact-refuse" @click="quickAction('refuse')">
              <span class="mact-ico">✕</span><span>Refuser</span>
            </button>
            <button class="mact-btn mact-report" @click="rdvModal.action = 'reporter'">
              <span class="mact-ico">📅</span><span>Reporter</span>
            </button>
            <button class="mact-btn mact-heure" @click="rdvModal.action = 'heure'">
              <span class="mact-ico">🕐</span><span>Modifier l'heure</span>
            </button>
            <button class="mact-btn mact-date" @click="rdvModal.action = 'date'">
              <span class="mact-ico">📆</span><span>Modifier la date</span>
            </button>
            <button class="mact-btn mact-note" @click="rdvModal.action = 'remarque'">
              <span class="mact-ico">📝</span><span>Ajouter une remarque</span>
            </button>
          </div>

          <!-- Sous-formulaire : Reporter -->
          <div v-else-if="rdvModal.action === 'reporter'" class="modal-subform">
            <p class="subform-label">Nouvelle date et heure du RDV</p>
            <div style="display:flex;gap:10px;margin-bottom:16px">
              <input type="date" v-model="rdvModal.inputDate" class="form-inp" style="flex:1" />
              <input type="time" v-model="rdvModal.inputTime" class="form-inp" style="flex:1" />
            </div>
            <div class="subform-footer">
              <button class="subform-back" @click="rdvModal.action = null">Retour</button>
              <button class="subform-submit" @click="submitReporter()">Reporter le RDV</button>
            </div>
          </div>

          <!-- Sous-formulaire : Modifier l'heure -->
          <div v-else-if="rdvModal.action === 'heure'" class="modal-subform">
            <p class="subform-label">Nouvelle heure (date conservée)</p>
            <input type="time" v-model="rdvModal.inputTime" class="form-inp" style="width:100%;margin-bottom:16px" />
            <div class="subform-footer">
              <button class="subform-back" @click="rdvModal.action = null">Retour</button>
              <button class="subform-submit" @click="submitModifHeure()">Enregistrer</button>
            </div>
          </div>

          <!-- Sous-formulaire : Modifier la date -->
          <div v-else-if="rdvModal.action === 'date'" class="modal-subform">
            <p class="subform-label">Nouvelle date (heure conservée)</p>
            <input type="date" v-model="rdvModal.inputDate" class="form-inp" style="width:100%;margin-bottom:16px" />
            <div class="subform-footer">
              <button class="subform-back" @click="rdvModal.action = null">Retour</button>
              <button class="subform-submit" @click="submitModifDate()">Enregistrer</button>
            </div>
          </div>

          <!-- Sous-formulaire : Remarque -->
          <div v-else-if="rdvModal.action === 'remarque'" class="modal-subform">
            <p class="subform-label">Remarque / note interne</p>
            <textarea v-model="rdvModal.inputNote" class="form-inp" rows="4"
              placeholder="Saisissez une remarque..." style="width:100%;resize:vertical;margin-bottom:16px"></textarea>
            <div class="subform-footer">
              <button class="subform-back" @click="rdvModal.action = null">Retour</button>
              <button class="subform-submit" @click="submitRemarque()">Enregistrer</button>
            </div>
          </div>
        </div>
      </div>
    </Teleport>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const API   = `http://${window.location.hostname}:8081/api/receptionniste`
const token = localStorage.getItem('token')
const userRaw = JSON.parse(localStorage.getItem('user') || '{}')

const activeNav       = ref('home')
const sidebarOpen     = ref(false)
const loading         = ref(false)
const loadingMedecins = ref(false)
const loadingRdv      = ref(false)
const profil          = ref(null)
const medecins        = ref([])
const rdvs            = ref([])
const rdvFilter       = ref('tous')

const rdvFilters = [
  { key: 'tous',     label: 'Tous' },
  { key: 'planifie', label: 'Planifiés' },
  { key: 'confirme', label: 'Confirmés' },
  { key: 'refuse',   label: 'Refusés' },
  { key: 'reporte',  label: 'Reportés' },
  { key: 'annule',   label: 'Annulés' },
  { key: 'termine',  label: 'Terminés' },
]

const STATUT_LABELS = { planifie:'Planifié', confirme:'Confirmé', annule:'Annulé', termine:'Terminé', refuse:'Refusé', reporte:'Reporté' }
function statutLabel(s) { return STATUT_LABELS[s] || s }

const rdvModal = ref({ show: false, rdv: null, action: null, inputDate: '', inputTime: '', inputNote: '' })

const filteredRdvs = computed(() =>
  rdvFilter.value === 'tous' ? rdvs.value : rdvs.value.filter(r => r.statut === rdvFilter.value)
)

const rdvStats = computed(() => {
  const d = new Date()
  const todayStr = `${String(d.getDate()).padStart(2,'0')}/${String(d.getMonth()+1).padStart(2,'0')}/${d.getFullYear()}`
  return {
    today:     rdvs.value.filter(r => r.dateHeure?.startsWith(todayStr)).length,
    confirmed: rdvs.value.filter(r => r.statut === 'confirme').length
  }
})

function rdvCount(key) {
  return key === 'tous' ? rdvs.value.length : rdvs.value.filter(r => r.statut === key).length
}

function pInitials(prenom, nom) { return ((prenom?.[0] || '') + (nom?.[0] || '')).toUpperCase() || '?' }
function pColor(nom) { return avatarColors[(nom?.charCodeAt(0) || 0) % avatarColors.length] }

const navItems = [
  { key: 'home',     label: 'Tableau de bord',   icon: '<svg width="18" height="18" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" d="M3 12l2-2m0 0l7-7 7 7M5 10v10a1 1 0 001 1h3m10-11l2 2m-2-2v10a1 1 0 01-1 1h-3m-6 0a1 1 0 001-1v-4a1 1 0 011-1h2a1 1 0 011 1v4a1 1 0 001 1m-6 0h6"/></svg>' },
  { key: 'rdv',      label: 'Gestion des RDV',    icon: '<svg width="18" height="18" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24"><rect x="3" y="4" width="18" height="18" rx="2"/><path stroke-linecap="round" d="M16 2v4M8 2v4M3 10h18"/></svg>' },
  { key: 'medecins', label: 'Médecins du cabinet', icon: '<svg width="18" height="18" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z"/></svg>' },
  { key: 'profil',   label: 'Mon profil',          icon: '<svg width="18" height="18" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" d="M5.121 17.804A13.937 13.937 0 0112 16c2.5 0 4.847.655 6.879 1.804M15 10a3 3 0 11-6 0 3 3 0 016 0M19 21H5a2 2 0 01-2-2v-1a2 2 0 012-2h14a2 2 0 012 2v1a2 2 0 01-2 2z"/></svg>' },
]

const userName     = computed(() => `${userRaw.prenom || ''} ${userRaw.nom || ''}`.trim() || userRaw.email || '')
const userInitials = computed(() => {
  const n = userRaw.nom || ''; const p = userRaw.prenom || ''
  return ((p[0] || '') + (n[0] || '')).toUpperCase() || '?'
})
const avatarColors = ['#0891b2','#7c3aed','#2563eb','#be185d','#059669']
const avatarColor  = computed(() => avatarColors[(userRaw.nom?.charCodeAt(0) || 0) % avatarColors.length])

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
function mInitials(m) { return ((m.prenom?.[0] || '') + (m.nom?.[0] || '')).toUpperCase() }
function mColor(nom) { return avatarColors[(nom?.charCodeAt(0) || 0) % avatarColors.length] }

function logout() {
  localStorage.removeItem('token')
  localStorage.removeItem('user')
  router.push('/login')
}

async function fetchMedecins() {
  loadingMedecins.value = true
  try {
    const res = await fetch(`${API}/medecins`, { headers: hdrs() })
    medecins.value = res.ok ? await res.json() : []
  } finally {
    loadingMedecins.value = false
  }
}

async function fetchRdvs() {
  loadingRdv.value = true
  try {
    const res = await fetch(`${API}/rdv`, { headers: hdrs() })
    rdvs.value = res.ok ? await res.json() : []
  } finally {
    loadingRdv.value = false
  }
}

function openModal(rdv) {
  rdvModal.value = { show: true, rdv, action: null, inputDate: '', inputTime: '', inputNote: '' }
}
function closeModal() { rdvModal.value.show = false }

function parseDateHeure(str) {
  if (!str) return { date: '', time: '00:00' }
  const [datePart, timePart] = str.split(' ')
  const [day, month, year] = datePart.split('/')
  return { date: `${year}-${month}-${day}`, time: timePart || '00:00' }
}

async function patchRdv(id, body) {
  const res = await fetch(`${API}/rdv/${id}`, {
    method: 'PATCH', headers: hdrs(), body: JSON.stringify(body)
  })
  if (res.ok) {
    const updated = await res.json()
    const idx = rdvs.value.findIndex(r => r.idRdv === id)
    if (idx !== -1) rdvs.value[idx] = updated
    closeModal()
  } else {
    alert('Erreur lors de la mise à jour')
  }
}

async function quickAction(statut) { await patchRdv(rdvModal.value.rdv.idRdv, { statut }) }

async function submitReporter() {
  const { inputDate, inputTime, rdv } = rdvModal.value
  if (!inputDate || !inputTime) { alert('Veuillez sélectionner une date et une heure'); return }
  await patchRdv(rdv.idRdv, { statut: 'reporte', dateHeure: `${inputDate}T${inputTime}` })
}

async function submitModifHeure() {
  const { inputTime, rdv } = rdvModal.value
  if (!inputTime) { alert('Veuillez sélectionner une heure'); return }
  const { date } = parseDateHeure(rdv.dateHeure)
  await patchRdv(rdv.idRdv, { dateHeure: `${date}T${inputTime}` })
}

async function submitModifDate() {
  const { inputDate, rdv } = rdvModal.value
  if (!inputDate) { alert('Veuillez sélectionner une date'); return }
  const { time } = parseDateHeure(rdv.dateHeure)
  await patchRdv(rdv.idRdv, { dateHeure: `${inputDate}T${time}` })
}

async function submitRemarque() {
  const { inputNote, rdv } = rdvModal.value
  if (!inputNote.trim()) { alert('Veuillez saisir une remarque'); return }
  await patchRdv(rdv.idRdv, { notes: inputNote.trim() })
}

onMounted(async () => {
  loading.value = true
  try {
    const res = await fetch(`${API}/profil`, { headers: hdrs() })
    if (res.status === 401) { router.push('/login'); return }
    profil.value = await res.json()
    await Promise.all([fetchMedecins(), fetchRdvs()])
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

.btn-refresh { display:flex;align-items:center;gap:6px;padding:7px 14px;border-radius:8px;border:1px solid #e2e8f0;background:#fff;color:#475569;font-size:13px;font-weight:500;cursor:pointer;transition:all .15s; }
.btn-refresh:hover:not(:disabled) { background:#f8fafc;border-color:#cbd5e1; }
.btn-refresh:disabled { opacity:.5;cursor:default; }

.rdv-table-wrap { background:#fff;border-radius:14px;border:1px solid #e2e8f0;overflow:hidden; }
.rdv-table-scroll { overflow-x:auto; }
.rdv-table { width:100%;border-collapse:collapse;font-size:13px; }
.rdv-table th { padding:12px 14px;text-align:left;font-size:11px;font-weight:600;text-transform:uppercase;letter-spacing:.05em;color:#94a3b8;background:#f8fafc;border-bottom:1px solid #e2e8f0; }
.rdv-table td { padding:12px 14px;border-bottom:1px solid #f1f5f9;vertical-align:middle; }
.rdv-table tr:last-child td { border-bottom:none; }
.rdv-table tr:hover td { background:#f8fafc; }

.cell-name { display:flex;align-items:center;gap:10px; }
.mini-avatar { width:30px;height:30px;border-radius:50%;display:flex;align-items:center;justify-content:center;color:#fff;font-size:11px;font-weight:700;flex-shrink:0; }

.statut-badge { display:inline-block;padding:3px 10px;border-radius:20px;font-size:11px;font-weight:600; }
.statut-planifie,.statut-planifié { background:#eff6ff;color:#2563eb; }
.statut-confirme,.statut-confirmé { background:#f0fdf4;color:#16a34a; }
.statut-annule,.statut-annulé   { background:#fff1f2;color:#dc2626; }
.statut-termine,.statut-terminé  { background:#f1f5f9;color:#64748b; }

.action-btn { width:28px;height:28px;border-radius:6px;border:none;cursor:pointer;font-size:13px;display:flex;align-items:center;justify-content:center;transition:all .15s; }
.action-btn.confirm { background:#dcfce7;color:#16a34a; }
.action-btn.confirm:hover { background:#bbf7d0; }
.action-btn.cancel  { background:#fee2e2;color:#dc2626; }
.action-btn.cancel:hover { background:#fecaca; }
.action-btn.done    { background:#f1f5f9;color:#475569; }
.action-btn.done:hover { background:#e2e8f0; }

.filter-btn { padding:6px 14px;border-radius:20px;border:1px solid #e2e8f0;background:#fff;font-size:12px;font-weight:500;color:#64748b;cursor:pointer;display:flex;align-items:center;gap:6px;transition:all .15s; }
.filter-btn:hover { border-color:#cbd5e1;background:#f8fafc; }
.filter-btn.active { background:#2563eb;color:#fff;border-color:#2563eb; }
.filter-count { background:rgba(255,255,255,.25);border-radius:20px;padding:0 6px;font-size:11px; }
.filter-btn:not(.active) .filter-count { background:#f1f5f9;color:#94a3b8; }

.statut-refuse,.statut-refusé { background:#fee2e2;color:#dc2626; }
.statut-reporte,.statut-reporté { background:#fff7ed;color:#ea580c; }

.actions-trigger { display:inline-flex;align-items:center;gap:5px;padding:6px 12px;border-radius:8px;border:1px solid #e2e8f0;background:#fff;color:#475569;font-size:12px;font-weight:500;cursor:pointer;transition:all .15s;white-space:nowrap; }
.actions-trigger:hover { background:#f8fafc;border-color:#cbd5e1;color:#1e293b; }

/* Modal */
.modal-overlay { position:fixed;inset:0;background:rgba(15,23,42,.55);backdrop-filter:blur(3px);z-index:1000;display:flex;align-items:center;justify-content:center;padding:16px; }
.modal-box { background:#fff;border-radius:16px;width:100%;max-width:440px;box-shadow:0 20px 60px rgba(0,0,0,.2);overflow:hidden; }
.modal-header { display:flex;align-items:flex-start;justify-content:space-between;padding:20px 24px 16px;border-bottom:1px solid #f1f5f9; }
.modal-title { margin:0;font-size:16px;font-weight:700;color:#1e293b; }
.modal-sub { margin:4px 0 0;font-size:12px;color:#64748b; }
.modal-close { width:30px;height:30px;border-radius:50%;border:none;background:#f1f5f9;color:#64748b;font-size:14px;cursor:pointer;display:flex;align-items:center;justify-content:center;flex-shrink:0;transition:all .15s; }
.modal-close:hover { background:#e2e8f0;color:#1e293b; }

.modal-actions-grid { display:grid;grid-template-columns:1fr 1fr;gap:10px;padding:20px 24px; }
.mact-btn { display:flex;flex-direction:column;align-items:center;gap:6px;padding:16px 12px;border-radius:12px;border:1.5px solid #e2e8f0;background:#f8fafc;cursor:pointer;font-size:13px;font-weight:500;color:#475569;transition:all .15s; }
.mact-btn:hover { border-color:#cbd5e1;background:#f1f5f9; }
.mact-ico { font-size:22px; }
.mact-confirm { border-color:#bbf7d0;background:#f0fdf4;color:#16a34a; }
.mact-confirm:hover { background:#dcfce7;border-color:#86efac; }
.mact-refuse { border-color:#fecaca;background:#fff1f2;color:#dc2626; }
.mact-refuse:hover { background:#fee2e2;border-color:#fca5a5; }
.mact-report { border-color:#fed7aa;background:#fff7ed;color:#ea580c; }
.mact-report:hover { background:#ffedd5;border-color:#fdba74; }
.mact-heure { border-color:#e9d5ff;background:#faf5ff;color:#7c3aed; }
.mact-heure:hover { background:#f3e8ff;border-color:#d8b4fe; }
.mact-date { border-color:#bfdbfe;background:#eff6ff;color:#2563eb; }
.mact-date:hover { background:#dbeafe;border-color:#93c5fd; }
.mact-note { border-color:#d1d5db;background:#f9fafb;color:#374151; }
.mact-note:hover { background:#f3f4f6;border-color:#9ca3af; }

.modal-subform { padding:20px 24px; }
.subform-label { margin:0 0 10px;font-size:13px;font-weight:600;color:#374151; }
.form-inp { border:1.5px solid #e2e8f0;border-radius:8px;padding:9px 12px;font-size:13px;color:#1e293b;outline:none;box-sizing:border-box;transition:border-color .15s; }
.form-inp:focus { border-color:#2563eb; }
.form-inp::placeholder { color:#94a3b8; }
.subform-footer { display:flex;gap:10px;justify-content:flex-end; }
.subform-back { padding:8px 16px;border-radius:8px;border:1px solid #e2e8f0;background:#fff;color:#64748b;font-size:13px;cursor:pointer;transition:all .15s; }
.subform-back:hover { background:#f8fafc; }
.subform-submit { padding:8px 20px;border-radius:8px;border:none;background:#2563eb;color:#fff;font-size:13px;font-weight:600;cursor:pointer;transition:all .15s; }
.subform-submit:hover { background:#1d4ed8; }
</style>
