<template>
  <div class="auth-root">

    <!-- ══ PANNEAU GAUCHE — Brand ══ -->
    <div class="auth-left">
      <div class="auth-left-inner">

        <div class="brand-logo">
          <svg width="22" height="22" fill="none" stroke="white" stroke-width="2" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" d="M4.318 6.318a4.5 4.5 0 000 6.364L12 20.364l7.682-7.682a4.5 4.5 0 00-6.364-6.364L12 7.636l-1.318-1.318a4.5 4.5 0 00-6.364 0z"/>
          </svg>
        </div>
        <h1 class="brand-name">AppSanté</h1>
        <p class="brand-tagline">Votre santé, notre priorité</p>

        <div class="brand-features">
          <div class="bf-item" v-for="f in features" :key="f">
            <div class="bf-icon">
              <svg width="13" height="13" fill="none" stroke="white" stroke-width="2.5" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" d="M5 13l4 4L19 7"/></svg>
            </div>
            <span>{{ f }}</span>
          </div>
        </div>

        <div class="brand-stats">
          <div class="bs-item"><p class="bs-num">43</p><p class="bs-lbl">Spécialités</p></div>
          <div class="bs-sep"></div>
          <div class="bs-item"><p class="bs-num">60</p><p class="bs-lbl">Villes</p></div>
          <div class="bs-sep"></div>
          <div class="bs-item"><p class="bs-num">1200+</p><p class="bs-lbl">Patients</p></div>
        </div>

      </div>
    </div>

    <!-- ══ PANNEAU DROIT — Formulaire ══ -->
    <div class="auth-right">
      <div class="auth-form-wrap">

        <!-- Onglets -->
        <div class="auth-tabs" :class="{ 'tabs-single': loginType === 'pro' }">
          <button :class="['auth-tab', { active: mode === 'login' || loginType === 'pro' }]" @click="mode = 'login'">
            Se connecter
          </button>
          <button v-if="loginType === 'patient'" :class="['auth-tab', { active: mode === 'register' }]" @click="mode = 'register'">
            Créer un compte
          </button>
          <div class="auth-tab-indicator" :style="loginType === 'pro' ? 'width:calc(100% - 10px);left:0' : mode === 'register' ? 'left:50%' : 'left:0'"></div>
        </div>

        <!-- Alert -->
        <div v-if="alert.show" :class="['auth-alert', `alert-${alert.type}`]">
          <svg v-if="alert.type === 'error'" width="16" height="16" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" d="M12 8v4m0 4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"/></svg>
          <svg v-else width="16" height="16" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z"/></svg>
          <span>{{ alert.message }}</span>
        </div>

        <!-- ══ CONNEXION ══ -->
        <Transition name="slide" mode="out-in">
        <div v-if="mode === 'login'" key="login">

          <!-- Toggle Patient / Pro -->
          <div class="login-type-toggle">
            <button :class="['ltt-btn', { active: loginType === 'patient' }]" @click="loginType = 'patient'">
              <svg width="15" height="15" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z"/></svg>
              Patient
            </button>
            <button :class="['ltt-btn', { active: loginType === 'pro' }]" @click="loginType = 'pro'">
              <svg width="15" height="15" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" d="M9 12l2 2 4-4m5.618-4.016A11.955 11.955 0 0112 2.944a11.955 11.955 0 01-8.618 3.04A12.02 12.02 0 003 9c0 5.591 3.824 10.29 9 11.622 5.176-1.332 9-6.03 9-11.622 0-1.042-.133-2.052-.382-3.016z"/></svg>
              Professionnel
            </button>
          </div>

          <div class="form-header">
            <h2>Bon retour 👋</h2>
            <p v-if="loginType === 'patient'">Connectez-vous à votre espace patient</p>
            <p v-else style="color:#7c3aed">Accès Admin · Médecin · Réceptionniste</p>
          </div>

          <form @submit.prevent="handleLogin">
            <div class="field-group">
              <label>Adresse email</label>
              <div class="field-wrap">
                <svg class="field-icon" width="16" height="16" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" d="M3 8l7.89 5.26a2 2 0 002.22 0L21 8M5 19h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v10a2 2 0 002 2z"/></svg>
                <input v-model="login.email" type="email" placeholder="vous@exemple.com" class="field-input" autofocus/>
              </div>
            </div>

            <div class="field-group">
              <label>Mot de passe</label>
              <div class="field-wrap">
                <svg class="field-icon" width="16" height="16" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" d="M12 15v2m-6 4h12a2 2 0 002-2v-6a2 2 0 00-2-2H6a2 2 0 00-2 2v6a2 2 0 002 2zm10-10V7a4 4 0 00-8 0v4h8z"/></svg>
                <input v-model="login.motDePasse" :type="showPwd ? 'text' : 'password'" placeholder="••••••••" class="field-input icon-left"/>
                <button type="button" class="field-eye" @click="showPwd = !showPwd">
                  <svg width="16" height="16" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z"/><path stroke-linecap="round" stroke-linejoin="round" d="M2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z"/></svg>
                </button>
              </div>
            </div>

            <button type="submit" class="btn-submit" :disabled="loadingLogin"
              :style="loginType === 'pro' ? 'background:linear-gradient(135deg,#7c3aed,#6d28d9)' : ''">
              <svg v-if="loadingLogin" class="spin" width="18" height="18" fill="none" viewBox="0 0 24 24"><circle cx="12" cy="12" r="10" stroke="rgba(255,255,255,.3)" stroke-width="3"/><path fill="#fff" d="M4 12a8 8 0 018-8v4a4 4 0 00-4 4H4z"/></svg>
              {{ loadingLogin ? 'Connexion…' : 'Se connecter' }}
            </button>
          </form>

          <p v-if="loginType === 'patient'" class="auth-switch">
            Pas de compte ?
            <button @click="mode = 'register'" class="auth-switch-btn">Créer un compte →</button>
          </p>
          <p v-else class="auth-switch" style="color:#94a3b8;font-size:12px;text-align:center">
            Votre compte est créé par l'administrateur système
          </p>
        </div>

        <!-- ══ INSCRIPTION ══ -->
        <div v-else key="register">
          <div class="form-header">
            <h2>Créer un compte</h2>
            <p>Étape {{ step }} sur 3 — {{ stepLabels[step - 1] }}</p>
          </div>

          <!-- Stepper -->
          <div class="stepper">
            <template v-for="(lbl, i) in stepLabels" :key="lbl">
              <div :class="['step-dot', { active: step === i+1, done: step > i+1 }]">
                <svg v-if="step > i+1" width="13" height="13" fill="none" stroke="white" stroke-width="2.8" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" d="M5 13l4 4L19 7"/></svg>
                <span v-else>{{ i+1 }}</span>
              </div>
              <div v-if="i < 2" :class="['step-line', { filled: step > i+1 }]"></div>
            </template>
          </div>

          <form @submit.prevent="handleRegisterSubmit">
            <Transition name="slide" mode="out-in">

              <!-- Step 1 -->
              <div v-if="step === 1" key="s1">
                <div class="field-group">
                  <label>Email <span class="req">*</span></label>
                  <div class="field-wrap">
                    <svg class="field-icon" width="16" height="16" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" d="M3 8l7.89 5.26a2 2 0 002.22 0L21 8M5 19h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v10a2 2 0 002 2z"/></svg>
                    <input v-model="reg.email" type="email" placeholder="vous@exemple.com" class="field-input" autofocus/>
                  </div>
                  <p v-if="errs.email" class="err-msg">{{ errs.email }}</p>
                </div>
                <div class="field-group">
                  <label>Mot de passe <span class="req">*</span></label>
                  <div class="field-wrap">
                    <svg class="field-icon" width="16" height="16" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" d="M12 15v2m-6 4h12a2 2 0 002-2v-6a2 2 0 00-2-2H6a2 2 0 00-2 2v6a2 2 0 002 2zm10-10V7a4 4 0 00-8 0v4h8z"/></svg>
                    <input v-model="reg.motDePasse" :type="showPwd2 ? 'text' : 'password'" placeholder="Minimum 8 caractères" class="field-input icon-left"/>
                    <button type="button" class="field-eye" @click="showPwd2 = !showPwd2">
                      <svg width="16" height="16" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z"/><path stroke-linecap="round" stroke-linejoin="round" d="M2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z"/></svg>
                    </button>
                  </div>
                  <p v-if="errs.motDePasse" class="err-msg">{{ errs.motDePasse }}</p>
                  <div class="strength-row">
                    <div v-for="i in 4" :key="i" class="strength-bar" :style="{ background: i <= strength.level ? strength.color : '#e2e8f0' }"></div>
                    <span class="strength-label" :style="{ color: strength.color }">{{ strength.text }}</span>
                  </div>
                </div>
              </div>

              <!-- Step 2 -->
              <div v-else-if="step === 2" key="s2">
                <div class="fields-2col">
                  <div class="field-group">
                    <label>Prénom <span class="req">*</span></label>
                    <div class="field-wrap">
                      <svg class="field-icon" width="16" height="16" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z"/></svg>
                      <input v-model="reg.prenom" type="text" placeholder="Youssef" class="field-input" autofocus/>
                    </div>
                    <p v-if="errs.prenom" class="err-msg">{{ errs.prenom }}</p>
                  </div>
                  <div class="field-group">
                    <label>Nom <span class="req">*</span></label>
                    <div class="field-wrap">
                      <svg class="field-icon" width="16" height="16" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z"/></svg>
                      <input v-model="reg.nom" type="text" placeholder="Alaoui" class="field-input"/>
                    </div>
                    <p v-if="errs.nom" class="err-msg">{{ errs.nom }}</p>
                  </div>
                </div>
                <div v-if="reg.prenom || reg.nom" class="preview-card">
                  <div class="preview-avatar">{{ regInitials }}</div>
                  <div>
                    <p class="preview-name">{{ reg.prenom }} {{ reg.nom }}</p>
                    <p class="preview-email">{{ reg.email }}</p>
                  </div>
                </div>
              </div>

              <!-- Step 3 -->
              <div v-else key="s3">
                <div class="field-group">
                  <label>Téléphone <span class="opt">(optionnel)</span></label>
                  <div class="field-wrap">
                    <svg class="field-icon" width="16" height="16" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" d="M3 5a2 2 0 012-2h3.28a1 1 0 01.948.684l1.498 4.493a1 1 0 01-.502 1.21l-2.257 1.13a11.042 11.042 0 005.516 5.516l1.13-2.257a1 1 0 011.21-.502l4.493 1.498a1 1 0 01.684.949V19a2 2 0 01-2 2h-1C9.716 21 3 14.284 3 6V5z"/></svg>
                    <input v-model="reg.telephone" type="tel" placeholder="0612 345 678" class="field-input" autofocus/>
                  </div>
                </div>
                <div class="field-group">
                  <label>Ville <span class="opt">(optionnel)</span></label>
                  <div class="field-wrap">
                    <svg class="field-icon" width="16" height="16" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" d="M17.657 16.657L13.414 20.9a1.998 1.998 0 01-2.827 0l-4.244-4.243a8 8 0 1111.314 0z"/><path stroke-linecap="round" stroke-linejoin="round" d="M15 11a3 3 0 11-6 0 3 3 0 016 0z"/></svg>
                    <input v-model="reg.adresse" type="text" placeholder="Casablanca" class="field-input"/>
                  </div>
                </div>
                <div class="next-steps-box">
                  <p class="ns-title">Ce qui se passe ensuite</p>
                  <div v-for="ns in nextSteps" :key="ns" class="ns-item">
                    <div class="ns-icon"><svg width="12" height="12" fill="none" stroke="#2563eb" stroke-width="2.5" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" d="M5 13l4 4L19 7"/></svg></div>
                    <span>{{ ns }}</span>
                  </div>
                </div>
              </div>

            </Transition>

            <div class="form-nav">
              <button v-if="step > 1" type="button" class="btn-back-auth" @click="step--">← Précédent</button>
              <button type="submit" class="btn-submit" :style="step > 1 ? 'flex:1' : 'width:100%'" :disabled="loadingReg">
                <svg v-if="loadingReg" class="spin" width="18" height="18" fill="none" viewBox="0 0 24 24"><circle cx="12" cy="12" r="10" stroke="rgba(255,255,255,.3)" stroke-width="3"/><path fill="#fff" d="M4 12a8 8 0 018-8v4a4 4 0 00-4 4H4z"/></svg>
                {{ step < 3 ? 'Suivant →' : (loadingReg ? 'Création…' : 'Créer mon compte') }}
              </button>
            </div>
          </form>

          <p class="auth-switch">
            Déjà inscrit ?
            <button @click="mode = 'login'" class="auth-switch-btn">Se connecter →</button>
          </p>
        </div>
        </Transition>

      </div>
    </div>

  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const API    = 'http://localhost:8081'

const mode       = ref('login')
const loginType  = ref('patient')
const showPwd    = ref(false)
const showPwd2   = ref(false)
const alert      = ref({ show: false, type: '', message: '' })
const loadingLogin = ref(false)
const loadingReg   = ref(false)

// ── Login ──
const login = ref({ email: '', motDePasse: '' })

async function handleLogin() {
  alert.value.show = false
  loadingLogin.value = true
  try {
    const res  = await fetch(`${API}/api/auth/login`, {
      method: 'POST', headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ email: login.value.email, motDePasse: login.value.motDePasse })
    })
    const data = await res.json()
    if (res.ok) {
      const ROLE_HOME = { admin: '/admin', patient: '/dashboard', medecin: '/medecin', receptionniste: '/receptionniste' }
      localStorage.setItem('token', data.token)
      localStorage.setItem('user', JSON.stringify({ id: data.id, email: data.email, role: data.role, nom: data.nom, prenom: data.prenom }))
      router.push(ROLE_HOME[data.role] || '/dashboard')
    } else {
      alert.value = { show: true, type: 'error', message: data.message || 'Email ou mot de passe incorrect.' }
    }
  } catch {
    alert.value = { show: true, type: 'error', message: 'Impossible de joindre le serveur.' }
  } finally {
    loadingLogin.value = false
  }
}

// ── Register ──
const step  = ref(1)
const stepLabels = ['Identifiants', 'Identité', 'Coordonnées']
const nextSteps  = ['Compte créé instantanément', 'Dossier médical automatique', 'Premier RDV en ligne']

const reg  = ref({ email: '', motDePasse: '', nom: '', prenom: '', telephone: '', adresse: '' })
const errs = ref({ email: '', motDePasse: '', nom: '', prenom: '' })

const strength = computed(() => {
  const v = reg.value.motDePasse
  if (!v)                                                                                  return { level:0, color:'',        text:'' }
  if (v.length>=10 && /[A-Z]/.test(v) && /[0-9]/.test(v) && /[^a-zA-Z0-9]/.test(v))    return { level:4, color:'#22c55e', text:'Fort' }
  if (v.length>=8  && (/[A-Z]/.test(v) || /[0-9]/.test(v)))                              return { level:3, color:'#eab308', text:'Bon' }
  if (v.length>=6)                                                                         return { level:2, color:'#f97316', text:'Passable' }
  return { level:1, color:'#ef4444', text:'Faible' }
})

const regInitials = computed(() => {
  return ((reg.value.prenom[0] || '') + (reg.value.nom[0] || '')).toUpperCase() || '?'
})

function validateStep() {
  errs.value = { email:'', motDePasse:'', nom:'', prenom:'' }
  if (step.value === 1) {
    let ok = true
    if (!reg.value.email || !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(reg.value.email)) { errs.value.email = 'Email invalide.'; ok = false }
    if (!reg.value.motDePasse || reg.value.motDePasse.length < 8) { errs.value.motDePasse = 'Minimum 8 caractères.'; ok = false }
    return ok
  }
  if (step.value === 2) {
    let ok = true
    if (!reg.value.nom.trim())    { errs.value.nom    = 'Nom obligatoire.';    ok = false }
    if (!reg.value.prenom.trim()) { errs.value.prenom = 'Prénom obligatoire.'; ok = false }
    return ok
  }
  return true
}

function handleRegisterSubmit() {
  if (!validateStep()) return
  if (step.value < 3) { step.value++; return }
  handleRegister()
}

async function handleRegister() {
  alert.value.show = false
  loadingReg.value = true
  try {
    const res  = await fetch(`${API}/api/auth/register`, {
      method: 'POST', headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        email: reg.value.email.trim(), motDePasse: reg.value.motDePasse,
        nom: reg.value.nom.trim(), prenom: reg.value.prenom.trim(),
        telephone: reg.value.telephone.trim(), adresse: reg.value.adresse.trim()
      })
    })
    const data = await res.json()
    if (res.status === 201) {
      localStorage.setItem('token', data.token)
      localStorage.setItem('user', JSON.stringify({ id:data.id, email:data.email, role:data.role, prenom:reg.value.prenom, nom:reg.value.nom }))
      alert.value = { show: true, type: 'success', message: `Bienvenue ${reg.value.prenom} ! Votre compte est créé.` }
      setTimeout(() => router.push('/dashboard'), 1500)
    } else {
      alert.value = { show: true, type: 'error', message: data.message || 'Erreur lors de la création.' }
    }
  } catch {
    alert.value = { show: true, type: 'error', message: 'Impossible de joindre le serveur.' }
  } finally {
    loadingReg.value = false
  }
}

const features = [
  'Prise de RDV en ligne 24h/24',
  'Dossier médical sécurisé',
  '43 spécialités médicales',
  '60 villes partout au Maroc',
]
</script>

<style scoped>
/* ══ ROOT ══ */
.auth-root { display:flex; min-height:100vh; font-family:inherit; }

/* ══ LEFT PANEL ══ */
.auth-left {
  width:42%;
  background:linear-gradient(160deg,#0f172a 0%,#1e3a8a 50%,#1d4ed8 100%);
  display:flex; align-items:center; justify-content:center;
  padding:48px 40px;
  position:relative; overflow:hidden;
}
.auth-left::before {
  content:''; position:absolute; top:-80px; right:-80px;
  width:320px; height:320px; border-radius:50%;
  background:rgba(255,255,255,.04);
}
.auth-left::after {
  content:''; position:absolute; bottom:-100px; left:-60px;
  width:280px; height:280px; border-radius:50%;
  background:rgba(255,255,255,.03);
}
@media(max-width:768px){ .auth-left{ display:none; } }

.auth-left-inner { position:relative; z-index:1; max-width:320px; }

.brand-logo {
  width:52px; height:52px;
  background:linear-gradient(135deg,#3b82f6,#0ea5e9);
  border-radius:16px; display:flex; align-items:center; justify-content:center;
  margin-bottom:20px;
  box-shadow:0 8px 24px rgba(59,130,246,.4);
}
.brand-name { font-size:32px; font-weight:800; color:#fff; margin:0 0 6px; letter-spacing:-.02em; }
.brand-tagline { font-size:15px; color:rgba(255,255,255,.6); margin:0 0 40px; }

.brand-features { display:flex; flex-direction:column; gap:14px; margin-bottom:40px; }
.bf-item { display:flex; align-items:center; gap:12px; color:rgba(255,255,255,.85); font-size:14px; }
.bf-icon { width:24px; height:24px; background:rgba(59,130,246,.4); border-radius:6px; display:flex; align-items:center; justify-content:center; flex-shrink:0; }

.brand-stats { display:flex; align-items:center; background:rgba(255,255,255,.07); border:1px solid rgba(255,255,255,.1); border-radius:16px; padding:18px 22px; gap:0; }
.bs-item { flex:1; text-align:center; }
.bs-num { font-size:22px; font-weight:800; color:#fff; margin:0 0 2px; }
.bs-lbl { font-size:11px; color:rgba(255,255,255,.5); margin:0; font-weight:500; }
.bs-sep { width:1px; height:36px; background:rgba(255,255,255,.12); }

/* ══ RIGHT PANEL ══ */
.auth-right {
  flex:1; background:#f8fafc;
  display:flex; align-items:center; justify-content:center;
  padding:40px 32px; overflow-y:auto;
}
.auth-form-wrap { width:100%; max-width:400px; }

/* Tabs */
.auth-tabs { position:relative; display:grid; grid-template-columns:1fr 1fr; background:#fff; border-radius:14px; padding:5px; margin-bottom:24px; box-shadow:0 1px 4px rgba(0,0,0,.06); border:1px solid #e2e8f0; }
.auth-tab { padding:10px; border:none; background:transparent; font-size:14px; font-weight:600; color:#94a3b8; cursor:pointer; border-radius:10px; transition:color .2s; position:relative; z-index:1; }
.auth-tab.active { color:#fff; }
.auth-tab-indicator { position:absolute; top:5px; bottom:5px; width:calc(50% - 5px); background:linear-gradient(135deg,#2563eb,#1d4ed8); border-radius:10px; transition:all .25s cubic-bezier(.4,0,.2,1); box-shadow:0 4px 12px rgba(37,99,235,.3); }
.tabs-single { grid-template-columns:1fr; }

/* Login type toggle */
.login-type-toggle { display:flex; background:#f1f5f9; border-radius:10px; padding:4px; margin-bottom:20px; gap:4px; }
.ltt-btn { flex:1; display:flex; align-items:center; justify-content:center; gap:6px; padding:9px; border:none; background:transparent; font-size:13px; font-weight:600; color:#64748b; cursor:pointer; border-radius:7px; transition:all .2s; }
.ltt-btn.active { background:#fff; color:#2563eb; box-shadow:0 1px 4px rgba(0,0,0,.1); }
.ltt-btn:last-child.active { color:#7c3aed; }

/* Alert */
.auth-alert { display:flex; align-items:center; gap:10px; padding:12px 14px; border-radius:12px; margin-bottom:16px; font-size:13px; font-weight:500; }
.alert-error   { background:#fef2f2; color:#dc2626; border:1px solid #fecaca; }
.alert-success { background:#f0fdf4; color:#16a34a; border:1px solid #bbf7d0; }

/* Form header */
.form-header { margin-bottom:22px; }
.form-header h2 { font-size:22px; font-weight:800; color:#0f172a; margin:0 0 4px; }
.form-header p  { font-size:13px; color:#64748b; margin:0; }

/* Fields */
.field-group { margin-bottom:16px; }
.field-group label { display:block; font-size:13px; font-weight:600; color:#374151; margin-bottom:6px; }
.req { color:#ef4444; }
.opt { color:#94a3b8; font-weight:400; }
.field-wrap { position:relative; display:flex; align-items:center; background:#fff; border:1.5px solid #e2e8f0; border-radius:11px; transition:border-color .18s, box-shadow .18s; }
.field-wrap:focus-within { border-color:#2563eb; box-shadow:0 0 0 3px rgba(37,99,235,.1); }
.field-icon { position:absolute; left:13px; color:#94a3b8; flex-shrink:0; pointer-events:none; }
.field-input { width:100%; border:none; outline:none; padding:11px 13px 11px 38px; font-size:14px; color:#0f172a; background:transparent; border-radius:11px; }
.field-input.icon-left { padding-right:40px; }
.field-eye { position:absolute; right:12px; background:none; border:none; cursor:pointer; color:#94a3b8; display:flex; padding:2px; }
.field-eye:hover { color:#374151; }
.err-msg { font-size:12px; color:#dc2626; margin:4px 0 0; }

/* Strength */
.strength-row { display:flex; align-items:center; gap:4px; margin-top:8px; }
.strength-bar { flex:1; height:3px; border-radius:2px; transition:background .3s; }
.strength-label { font-size:11px; font-weight:600; min-width:50px; }

/* 2-col */
.fields-2col { display:grid; grid-template-columns:1fr 1fr; gap:12px; }

/* Preview */
.preview-card { display:flex; align-items:center; gap:12px; background:#f8fafc; border:1px solid #e2e8f0; border-radius:13px; padding:12px 14px; margin-top:4px; }
.preview-avatar { width:38px; height:38px; background:linear-gradient(135deg,#2563eb,#7c3aed); border-radius:10px; display:flex; align-items:center; justify-content:center; color:#fff; font-weight:800; font-size:14px; flex-shrink:0; }
.preview-name { font-size:14px; font-weight:700; color:#0f172a; margin:0 0 1px; }
.preview-email { font-size:12px; color:#64748b; margin:0; }

/* Next steps */
.next-steps-box { background:#fff; border:1px solid #e2e8f0; border-radius:14px; padding:14px 16px; }
.ns-title { font-size:10px; font-weight:700; color:#94a3b8; text-transform:uppercase; letter-spacing:.08em; margin:0 0 10px; }
.ns-item { display:flex; align-items:center; gap:10px; margin-bottom:8px; font-size:13px; color:#475569; }
.ns-item:last-child { margin-bottom:0; }
.ns-icon { width:22px; height:22px; background:#eff6ff; border-radius:6px; display:flex; align-items:center; justify-content:center; flex-shrink:0; }

/* Stepper */
.stepper { display:flex; align-items:center; margin-bottom:22px; }
.step-dot { width:32px; height:32px; border-radius:50%; display:flex; align-items:center; justify-content:center; font-size:12px; font-weight:700; flex-shrink:0; transition:all .3s; background:#f1f5f9; color:#94a3b8; }
.step-dot.active { background:#2563eb; color:#fff; box-shadow:0 0 0 4px rgba(37,99,235,.15); }
.step-dot.done   { background:#22c55e; color:#fff; }
.step-line { flex:1; height:2px; background:#e2e8f0; margin:0 6px; transition:background .3s; }
.step-line.filled { background:#22c55e; }

/* Buttons */
.btn-submit { display:flex; align-items:center; justify-content:center; gap:8px; width:100%; padding:13px 24px; background:linear-gradient(135deg,#2563eb,#1d4ed8); color:#fff; border:none; border-radius:12px; font-size:15px; font-weight:700; cursor:pointer; transition:all .2s; box-shadow:0 4px 14px rgba(37,99,235,.3); margin-top:6px; }
.btn-submit:hover:not(:disabled) { transform:translateY(-1px); box-shadow:0 6px 20px rgba(37,99,235,.4); }
.btn-submit:disabled { opacity:.6; cursor:not-allowed; }

.btn-back-auth { padding:13px 18px; background:#f1f5f9; border:none; border-radius:12px; font-size:14px; font-weight:600; color:#64748b; cursor:pointer; transition:background .18s; }
.btn-back-auth:hover { background:#e2e8f0; }

.form-nav { display:flex; gap:10px; margin-top:8px; }

/* Switch link */
.auth-switch { text-align:center; font-size:13px; color:#64748b; margin-top:18px; }
.auth-switch-btn { background:none; border:none; color:#2563eb; font-size:13px; font-weight:700; cursor:pointer; padding:0; }

/* Animations */
.slide-enter-active, .slide-leave-active { transition:opacity .2s ease, transform .2s ease; }
.slide-enter-from { opacity:0; transform:translateX(16px); }
.slide-leave-to   { opacity:0; transform:translateX(-16px); }
.spin { animation:spin 1s linear infinite; }
@keyframes spin { to{ transform:rotate(360deg); } }
</style>
