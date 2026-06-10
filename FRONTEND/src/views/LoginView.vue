<template>
  <div style="background:#f1f5f9; min-height:100vh; display:flex; flex-direction:column;">


<div style="flex:1; display:flex;">

      <!-- FORM PANEL -->
      <div style="flex:1; display:flex; align-items:center; justify-content:center; padding:40px 32px;">
        <div style="width:100%; max-width:380px;">

          <div class="animate-fadeup" style="margin-bottom:28px;">
            <h1 style="font-size:26px; font-weight:800; color:#0f172a; margin:0 0 6px;">Connexion</h1>
            <p style="color:#64748b; font-size:14px; margin:0;">Accédez à votre espace patient</p>
          </div>

          <!-- Alert -->
          <div v-if="alert.show" :class="['alert-box', `alert-${alert.type}`]">
            <svg width="18" height="18" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z"/></svg>
            <span>{{ alert.message }}</span>
          </div>

          <!-- FORM -->
          <form @submit.prevent="handleSubmit">

            <!-- Email -->
            <div class="animate-fadeup delay-100" style="margin-bottom:16px;">
              <label style="display:block; font-size:13px; font-weight:600; color:#374151; margin-bottom:6px;">Adresse email</label>
              <div class="field-wrap">
                <input v-model="form.email" type="email" placeholder="vous@exemple.com" class="form-input"/>
                <svg class="field-icon" width="16" height="16" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" d="M3 8l7.89 5.26a2 2 0 002.22 0L21 8M5 19h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v10a2 2 0 002 2z"/></svg>
              </div>
            </div>

            <!-- Mot de passe -->
            <div class="animate-fadeup delay-150" style="margin-bottom:24px;">
              <label style="display:block; font-size:13px; font-weight:600; color:#374151; margin-bottom:6px;">Mot de passe</label>
              <div class="field-wrap" style="position:relative;">
                <input v-model="form.motDePasse" :type="showPassword ? 'text' : 'password'" placeholder="••••••••" class="form-input no-icon" style="padding-right:42px;"/>
                <button type="button" @click="showPassword = !showPassword" style="position:absolute; right:12px; top:50%; transform:translateY(-50%); background:none; border:none; cursor:pointer; color:#94a3b8; transition:color .2s;">
                  <svg width="18" height="18" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z"/>
                    <path stroke-linecap="round" stroke-linejoin="round" d="M2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z"/>
                  </svg>
                </button>
              </div>
            </div>

            <!-- SUBMIT -->
            <div class="animate-fadeup delay-200">
              <button type="submit" class="btn-primary">
                <span>Se connecter</span>
              </button>
            </div>

          </form>

          <!-- JWT notice -->
          <div class="animate-fadeup delay-250" style="margin-top:16px; background:#fffbeb; border:1.5px solid #fcd34d; border-radius:12px; padding:12px 16px; display:flex; align-items:flex-start; gap:10px;">
            <svg width="18" height="18" fill="none" stroke="#d97706" stroke-width="2" viewBox="0 0 24 24" style="flex-shrink:0; margin-top:1px;"><path stroke-linecap="round" stroke-linejoin="round" d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z"/></svg>
            <div>
              <p style="font-size:13px; font-weight:600; color:#92400e; margin:0 0 2px;">Étape 2 — Connexion JWT</p>
              <p style="font-size:12px; color:#b45309; margin:0;">La connexion sécurisée sera disponible prochainement.</p>
            </div>
          </div>

          <!-- Divider -->
          <div class="animate-fadeup delay-300" style="display:flex; align-items:center; gap:12px; margin:20px 0 16px;">
            <div style="flex:1; height:1px; background:#e2e8f0;"></div>
            <span style="font-size:12px; color:#94a3b8; font-weight:500;">OU</span>
            <div style="flex:1; height:1px; background:#e2e8f0;"></div>
          </div>

          <div class="animate-fadeup delay-400" style="text-align:center;">
            <p style="font-size:14px; color:#64748b; margin:0;">
              Pas encore de compte ?
              <RouterLink to="/register" style="color:#2563eb; font-weight:600; text-decoration:none;">Créer un compte gratuit →</RouterLink>
            </p>
          </div>

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
import { ref } from 'vue'

const form         = ref({ email: '', motDePasse: '' })
const showPassword = ref(false)
const alert        = ref({ show: false, type: 'warning', message: '' })

function handleSubmit() {
  alert.value = {
    show:    true,
    type:    'warning',
    message: 'La connexion JWT sera disponible à l\'étape 2.'
  }
}
</script>
