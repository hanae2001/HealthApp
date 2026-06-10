<template>
  <div style="background:#f1f5f9; min-height:100vh; display:flex; flex-direction:column;">


<!-- BODY -->
    <div style="flex:1; display:flex;">

      <!-- FORM PANEL -->
      <div style="flex:1; display:flex; align-items:center; justify-content:center; padding:40px 32px; overflow-y:auto;">
        <div style="width:100%; max-width:440px;">

          <!-- Title -->
          <div class="animate-fadeup" style="margin-bottom:6px;">
            <h1 style="font-size:26px; font-weight:800; color:#0f172a; margin:0 0 4px;">Créer un compte</h1>
            <p style="color:#64748b; font-size:14px; margin:0;">Étape {{ currentStep }} sur 3 — {{ steps[currentStep - 1].label }}</p>
          </div>

          <!-- Stepper indicator -->
          <div class="animate-fadeup delay-100" style="display:flex; align-items:center; margin-bottom:24px; padding-top:16px;">
            <template v-for="(step, index) in steps" :key="step.label">
              <div style="display:flex; flex-direction:column; align-items:center; gap:5px;">
                <div :style="{
                  width: '36px', height: '36px', borderRadius: '50%',
                  display: 'flex', alignItems: 'center', justifyContent: 'center',
                  fontWeight: '700', fontSize: '13px',
                  background: currentStep > index + 1 ? '#2563eb' : currentStep === index + 1 ? '#2563eb' : '#f1f5f9',
                  color: currentStep >= index + 1 ? '#fff' : '#94a3b8',
                  border: '2px solid ' + (currentStep >= index + 1 ? '#2563eb' : '#e2e8f0'),
                  boxShadow: currentStep === index + 1 ? '0 0 0 4px rgba(37,99,235,.12)' : 'none',
                  transition: 'all .35s cubic-bezier(.4,0,.2,1)'
                }">
                  <svg v-if="currentStep > index + 1" width="15" height="15" fill="none" stroke="currentColor" stroke-width="2.8" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" d="M5 13l4 4L19 7"/>
                  </svg>
                  <span v-else>{{ index + 1 }}</span>
                </div>
                <span :style="{
                  fontSize: '10px', fontWeight: '600', whiteSpace: 'nowrap',
                  color: currentStep >= index + 1 ? '#2563eb' : '#94a3b8',
                  transition: 'color .35s'
                }">{{ step.label }}</span>
              </div>
              <!-- Connecting line -->
              <div v-if="index < steps.length - 1" :style="{
                flex: 1, height: '2px',
                background: currentStep > index + 1 ? '#2563eb' : '#e2e8f0',
                transition: 'background .35s cubic-bezier(.4,0,.2,1)',
                margin: '0 6px', marginBottom: '18px'
              }"></div>
            </template>
          </div>

          <!-- Alert -->
          <div v-if="alert.show" :class="['alert-box', alert.type === 'success' ? 'alert-success' : 'alert-error']">
            <svg v-if="alert.type === 'success'" width="18" height="18" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z"/></svg>
            <svg v-else width="18" height="18" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" d="M12 8v4m0 4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"/></svg>
            <span>{{ alert.message }}</span>
          </div>

          <!-- Step content -->
          <form @submit.prevent="handleFormSubmit">
            <Transition name="step" mode="out-in">

              <!-- ─── STEP 1 : Identifiants ─── -->
              <div v-if="currentStep === 1" key="1">
                <div style="margin-bottom:16px;">
                  <label style="display:block; font-size:13px; font-weight:600; color:#374151; margin-bottom:6px;">
                    Adresse email <span style="color:#ef4444;">*</span>
                  </label>
                  <div class="field-wrap">
                    <input v-model="form.email" type="email" placeholder="vous@exemple.com" class="form-input" @input="errors.email = ''" autofocus/>
                    <svg class="field-icon" width="16" height="16" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" d="M3 8l7.89 5.26a2 2 0 002.22 0L21 8M5 19h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v10a2 2 0 002 2z"/></svg>
                  </div>
                  <p v-if="errors.email" class="err-msg">{{ errors.email }}</p>
                </div>

                <div style="margin-bottom:6px;">
                  <label style="display:block; font-size:13px; font-weight:600; color:#374151; margin-bottom:6px;">
                    Mot de passe <span style="color:#ef4444;">*</span>
                  </label>
                  <div class="field-wrap" style="position:relative;">
                    <input v-model="form.motDePasse" :type="showPassword ? 'text' : 'password'" placeholder="Minimum 8 caractères" class="form-input no-icon" style="padding-right:42px;" @input="errors.motDePasse = ''"/>
                    <button type="button" @click="showPassword = !showPassword" style="position:absolute; right:12px; top:50%; transform:translateY(-50%); background:none; border:none; cursor:pointer; color:#94a3b8;">
                      <svg width="18" height="18" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z"/>
                        <path stroke-linecap="round" stroke-linejoin="round" d="M2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z"/>
                      </svg>
                    </button>
                  </div>
                  <p v-if="errors.motDePasse" class="err-msg">{{ errors.motDePasse }}</p>
                </div>

                <!-- Strength bar -->
                <div style="display:flex; gap:4px; align-items:center; margin-bottom:8px; margin-top:8px;">
                  <div v-for="i in 4" :key="i" class="strength-bar" style="flex:1;" :style="{ background: i <= strength.level ? strength.color : '#e2e8f0' }"></div>
                  <span style="font-size:11px; margin-left:8px; min-width:56px; font-weight:500;" :style="{ color: strength.color || '#94a3b8' }">{{ strength.text }}</span>
                </div>
              </div>

              <!-- ─── STEP 2 : Identité ─── -->
              <div v-else-if="currentStep === 2" key="2">
                <div style="display:grid; grid-template-columns:1fr 1fr; gap:12px; margin-bottom:16px;">
                  <div>
                    <label style="display:block; font-size:13px; font-weight:600; color:#374151; margin-bottom:6px;">Nom <span style="color:#ef4444;">*</span></label>
                    <div class="field-wrap">
                      <input v-model="form.nom" type="text" placeholder="Alaoui" class="form-input" @input="errors.nom = ''" autofocus/>
                      <svg class="field-icon" width="16" height="16" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z"/></svg>
                    </div>
                    <p v-if="errors.nom" class="err-msg">{{ errors.nom }}</p>
                  </div>
                  <div>
                    <label style="display:block; font-size:13px; font-weight:600; color:#374151; margin-bottom:6px;">Prénom <span style="color:#ef4444;">*</span></label>
                    <div class="field-wrap">
                      <input v-model="form.prenom" type="text" placeholder="Youssef" class="form-input" @input="errors.prenom = ''"/>
                      <svg class="field-icon" width="16" height="16" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z"/></svg>
                    </div>
                    <p v-if="errors.prenom" class="err-msg">{{ errors.prenom }}</p>
                  </div>
                </div>

                <!-- Preview card -->
                <div v-if="form.nom || form.prenom" style="background:#f8fafc; border:1px solid #e2e8f0; border-radius:14px; padding:14px 16px; display:flex; align-items:center; gap:12px;">
                  <div style="width:40px; height:40px; background:linear-gradient(135deg,#2563eb,#1d4ed8); border-radius:50%; display:flex; align-items:center; justify-content:center; color:#fff; font-weight:700; font-size:16px; flex-shrink:0;">
                    {{ initials }}
                  </div>
                  <div>
                    <p style="font-size:14px; font-weight:700; color:#0f172a; margin:0;">{{ form.prenom }} {{ form.nom }}</p>
                    <p style="font-size:12px; color:#64748b; margin:0;">{{ form.email }}</p>
                  </div>
                </div>
              </div>

              <!-- ─── STEP 3 : Coordonnées ─── -->
              <div v-else key="3">
                <div style="margin-bottom:16px;">
                  <label style="display:block; font-size:13px; font-weight:600; color:#374151; margin-bottom:6px;">
                    Téléphone <span style="color:#94a3b8; font-weight:400;">(optionnel)</span>
                  </label>
                  <div class="field-wrap">
                    <input v-model="form.telephone" type="tel" placeholder="0612 345 678" class="form-input" autofocus/>
                    <svg class="field-icon" width="16" height="16" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" d="M3 5a2 2 0 012-2h3.28a1 1 0 01.948.684l1.498 4.493a1 1 0 01-.502 1.21l-2.257 1.13a11.042 11.042 0 005.516 5.516l1.13-2.257a1 1 0 011.21-.502l4.493 1.498a1 1 0 01.684.949V19a2 2 0 01-2 2h-1C9.716 21 3 14.284 3 6V5z"/></svg>
                  </div>
                </div>

                <div style="margin-bottom:20px;">
                  <label style="display:block; font-size:13px; font-weight:600; color:#374151; margin-bottom:6px;">
                    Ville / Adresse <span style="color:#94a3b8; font-weight:400;">(optionnel)</span>
                  </label>
                  <div class="field-wrap">
                    <input v-model="form.adresse" type="text" placeholder="Casablanca" class="form-input"/>
                    <svg class="field-icon" width="16" height="16" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" d="M17.657 16.657L13.414 20.9a1.998 1.998 0 01-2.827 0l-4.244-4.243a8 8 0 1111.314 0z"/><path stroke-linecap="round" stroke-linejoin="round" d="M15 11a3 3 0 11-6 0 3 3 0 016 0z"/></svg>
                  </div>
                </div>

                <!-- Ce qui se passe ensuite -->
                <div style="background:#fff; border:1px solid #e2e8f0; border-radius:16px; padding:16px 18px; margin-bottom:4px;">
                  <p style="font-size:11px; font-weight:700; color:#94a3b8; text-transform:uppercase; letter-spacing:.08em; margin:0 0 12px;">Ce qui se passe ensuite</p>
                  <div style="display:flex; flex-direction:column; gap:10px;">
                    <div v-for="step in nextSteps" :key="step" style="display:flex; align-items:center; gap:12px;">
                      <div style="width:26px; height:26px; background:#eff6ff; border-radius:8px; display:flex; align-items:center; justify-content:center; flex-shrink:0;">
                        <svg width="13" height="13" fill="none" stroke="#2563eb" stroke-width="2.5" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" d="M5 13l4 4L19 7"/></svg>
                      </div>
                      <span style="font-size:13px; color:#475569;">{{ step }}</span>
                    </div>
                  </div>
                </div>
              </div>

            </Transition>

            <!-- Navigation -->
            <div style="display:flex; gap:10px; margin-top:20px;">
              <button v-if="currentStep > 1" type="button" @click="prevStep" class="btn-secondary">
                <svg width="16" height="16" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" d="M15 19l-7-7 7-7"/></svg>
                Précédent
              </button>
              <button type="submit" class="btn-primary" style="flex:1;" :disabled="loading">
                <span v-if="currentStep < 3">Suivant</span>
                <span v-else>{{ loading ? 'Création en cours…' : 'Créer mon compte' }}</span>
                <svg v-if="loading" class="spin" width="18" height="18" fill="none" viewBox="0 0 24 24">
                  <circle cx="12" cy="12" r="10" stroke="rgba(255,255,255,.3)" stroke-width="3"/>
                  <path fill="#fff" d="M4 12a8 8 0 018-8v4a4 4 0 00-4 4H4z"/>
                </svg>
                <svg v-else-if="currentStep < 3" width="16" height="16" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" d="M9 5l7 7-7 7"/></svg>
              </button>
            </div>
          </form>

          <p class="animate-fadeup delay-500" style="text-align:center; font-size:12px; color:#94a3b8; margin-top:16px;">
            En créant un compte, vous acceptez nos
            <a href="#" style="color:#2563eb; text-decoration:none;">Conditions d'utilisation</a>
            et notre
            <a href="#" style="color:#2563eb; text-decoration:none;">Politique de confidentialité</a>.
          </p>

        </div>
      </div>
    </div>

    <!-- FOOTER -->
    <footer style="background:#fff; border-top:1px solid #e2e8f0; padding:14px 24px; text-align:center;">
      <span style="font-size:12px; color:#94a3b8;">AppSanté © 2026 — Tous droits réservés</span>
    </footer>

  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'

const router  = useRouter()
const API     = 'http://localhost:8081'

const currentStep  = ref(1)
const showPassword = ref(false)
const loading      = ref(false)
const alert        = ref({ show: false, type: '', message: '' })
const form = ref({ email: '', motDePasse: '', nom: '', prenom: '', telephone: '', adresse: '' })
const errors = ref({ email: '', motDePasse: '', nom: '', prenom: '' })

const steps = [
  { label: 'Identifiants' },
  { label: 'Identité'     },
  { label: 'Coordonnées'  }
]

const nextSteps = [
  'Votre compte est créé instantanément',
  'Un dossier médical vous est automatiquement attribué',
  'Prenez votre premier rendez-vous en ligne'
]

const strength = computed(() => {
  const v = form.value.motDePasse
  if (!v)                                                                                 return { level: 0, color: '',        text: '' }
  if (v.length >= 10 && /[A-Z]/.test(v) && /[0-9]/.test(v) && /[^a-zA-Z0-9]/.test(v)) return { level: 4, color: '#22c55e', text: 'Fort' }
  if (v.length >= 8  && (/[A-Z]/.test(v) || /[0-9]/.test(v)))                           return { level: 3, color: '#eab308', text: 'Bon' }
  if (v.length >= 6)                                                                      return { level: 2, color: '#f97316', text: 'Passable' }
  return { level: 1, color: '#ef4444', text: 'Faible' }
})

const initials = computed(() => {
  const n = form.value.nom.trim()[0]   || ''
  const p = form.value.prenom.trim()[0] || ''
  return (p + n).toUpperCase() || '?'
})

function validateStep(step) {
  errors.value = { email: '', motDePasse: '', nom: '', prenom: '' }
  if (step === 1) {
    let ok = true
    if (!form.value.email || !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(form.value.email)) { errors.value.email     = 'Veuillez saisir une adresse email valide.';           ok = false }
    if (!form.value.motDePasse || form.value.motDePasse.length < 8)                 { errors.value.motDePasse = 'Le mot de passe doit contenir au moins 8 caractères.'; ok = false }
    return ok
  }
  if (step === 2) {
    let ok = true
    if (!form.value.nom.trim())    { errors.value.nom    = 'Le nom est obligatoire.';    ok = false }
    if (!form.value.prenom.trim()) { errors.value.prenom = 'Le prénom est obligatoire.'; ok = false }
    return ok
  }
  return true
}

function nextStep() {
  if (validateStep(currentStep.value)) currentStep.value++
}

function prevStep() {
  alert.value.show = false
  currentStep.value--
}

function handleFormSubmit() {
  if (currentStep.value < 3) {
    nextStep()
  } else {
    handleSubmit()
  }
}

async function handleSubmit() {
  alert.value.show = false
  loading.value = true
  try {
    const res  = await fetch(`${API}/api/auth/register`, {
      method:  'POST',
      headers: { 'Content-Type': 'application/json' },
      body:    JSON.stringify({
        email:      form.value.email.trim(),
        motDePasse: form.value.motDePasse,
        nom:        form.value.nom.trim(),
        prenom:     form.value.prenom.trim(),
        telephone:  form.value.telephone.trim(),
        adresse:    form.value.adresse.trim()
      })
    })
    const data = await res.json()
    if (res.status === 201) {
      alert.value = { show: true, type: 'success', message: `Compte créé avec succès ! Bienvenue ${form.value.prenom} ${form.value.nom}.` }
      form.value = { email: '', motDePasse: '', nom: '', prenom: '', telephone: '', adresse: '' }
      setTimeout(() => router.push('/login'), 2500)
    } else {
      alert.value = { show: true, type: 'error', message: data.message || 'Une erreur est survenue. Veuillez réessayer.' }
    }
  } catch {
    alert.value = { show: true, type: 'error', message: 'Impossible de joindre le serveur. Vérifiez que le backend tourne sur le port 8081.' }
  } finally {
    loading.value = false
  }
}
</script>

<style>
.step-enter-active, .step-leave-active { transition: opacity .2s ease, transform .2s ease; }
.step-enter-from { opacity: 0; transform: translateX(18px); }
.step-leave-to   { opacity: 0; transform: translateX(-18px); }
</style>
