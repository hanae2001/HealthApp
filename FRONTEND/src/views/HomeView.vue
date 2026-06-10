<template>
  <div class="bg-slate-50 text-slate-800">

    <!-- NAVBAR -->
    <nav class="bg-white border-b border-slate-200 sticky top-0 z-50">
      <div class="max-w-6xl mx-auto px-6 py-4 flex items-center justify-between">
        <div class="flex items-center gap-3">
          <div class="w-9 h-9 bg-brand-600 rounded-xl flex items-center justify-center shadow-sm">
            <svg class="w-5 h-5 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2.5" d="M4.5 12h15m-7.5-7.5v15"/>
            </svg>
          </div>
          <span class="font-bold text-slate-900 text-lg tracking-tight">AppSanté</span>
        </div>
        <div class="hidden md:flex items-center gap-8 text-sm text-slate-500 font-medium">
          <a href="#features"    class="hover:text-brand-600 transition">Fonctionnalités</a>
          <a href="#medecins"    class="hover:text-brand-600 transition">Médecins</a>
          <a href="#specialites" class="hover:text-brand-600 transition">Spécialités</a>
        </div>
        <div class="flex items-center gap-3">
          <RouterLink to="/login"    class="text-sm font-medium text-slate-600 hover:text-brand-600 px-4 py-2 rounded-lg hover:bg-brand-50 transition">Connexion</RouterLink>
          <RouterLink to="/register" class="text-sm font-semibold bg-brand-600 hover:bg-brand-700 text-white px-5 py-2.5 rounded-xl shadow-sm transition">Commencer</RouterLink>
        </div>
      </div>
    </nav>

    <!-- HERO -->
    <section class="gradient-hero">
      <div class="max-w-6xl mx-auto px-6 py-20 md:py-28 flex flex-col md:flex-row items-center gap-12">
        <div class="flex-1 text-center md:text-left">
          <span class="inline-flex items-center gap-2 bg-brand-50 border border-brand-200 text-brand-700 text-xs font-semibold px-4 py-1.5 rounded-full mb-6">
            <span class="w-2 h-2 bg-brand-500 rounded-full animate-pulse"></span>
            Plateforme médicale certifiée
          </span>
          <h1 class="text-4xl md:text-5xl font-extrabold text-slate-900 leading-tight mb-5">
            Gérez votre santé<br/>
            <span class="text-brand-600">en toute simplicité</span>
          </h1>
          <p class="text-slate-500 text-lg mb-8 leading-relaxed max-w-lg">
            Prenez rendez-vous, consultez votre dossier médical et suivez vos traitements — depuis un seul espace sécurisé.
          </p>
          <div class="flex flex-col sm:flex-row gap-3 justify-center md:justify-start">
            <RouterLink to="/register" class="px-7 py-3.5 bg-brand-600 hover:bg-brand-700 text-white font-semibold rounded-xl shadow-md hover:shadow-lg transition text-sm">
              Créer un compte gratuit
            </RouterLink>
            <RouterLink to="/login" class="px-7 py-3.5 bg-white hover:bg-slate-50 text-slate-700 font-semibold rounded-xl border border-slate-200 shadow-sm transition text-sm">
              Se connecter
            </RouterLink>
          </div>
          <div class="flex items-center gap-4 mt-8 justify-center md:justify-start">
            <div class="flex -space-x-3">
              <img src="https://randomuser.me/api/portraits/women/44.jpg" class="w-9 h-9 rounded-full border-2 border-white object-cover shadow-sm" alt="patient"/>
              <img src="https://randomuser.me/api/portraits/men/32.jpg"   class="w-9 h-9 rounded-full border-2 border-white object-cover shadow-sm" alt="patient"/>
              <img src="https://randomuser.me/api/portraits/women/68.jpg" class="w-9 h-9 rounded-full border-2 border-white object-cover shadow-sm" alt="patient"/>
              <img src="https://randomuser.me/api/portraits/men/15.jpg"   class="w-9 h-9 rounded-full border-2 border-white object-cover shadow-sm" alt="patient"/>
            </div>
            <p class="text-sm text-slate-500"><span class="font-semibold text-slate-700">+2 000</span> patients inscrits</p>
          </div>
        </div>

        <!-- Card preview -->
        <div class="flex-1 flex justify-center">
          <div class="bg-white rounded-2xl shadow-xl border border-slate-200 p-6 w-full max-w-sm">
            <div class="flex items-center justify-between mb-4">
              <div class="flex items-center gap-3">
                <img src="https://randomuser.me/api/portraits/men/52.jpg" class="w-11 h-11 rounded-full object-cover border-2 border-brand-100" alt="Dr. Benali"/>
                <div>
                  <p class="text-xs text-slate-400 font-medium">Prochain rendez-vous</p>
                  <p class="font-semibold text-slate-800 text-sm">Dr. Hassan Benali</p>
                </div>
              </div>
              <span class="bg-green-50 text-green-700 text-xs font-semibold px-3 py-1 rounded-full border border-green-200">Confirmé</span>
            </div>
            <div class="bg-brand-50 rounded-xl p-4 mb-4 flex items-center gap-4">
              <div class="w-12 h-12 bg-brand-600 rounded-xl flex items-center justify-center text-white text-xl">🩺</div>
              <div>
                <p class="font-semibold text-slate-800 text-sm">Consultation médicale</p>
                <p class="text-slate-500 text-xs mt-0.5">Mercredi 7 mai · 10h30</p>
              </div>
            </div>
            <div class="grid grid-cols-3 gap-3">
              <div class="bg-slate-50 rounded-xl p-3 text-center">
                <p class="text-lg font-bold text-brand-600">4</p>
                <p class="text-xs text-slate-400">RDV total</p>
              </div>
              <div class="bg-slate-50 rounded-xl p-3 text-center">
                <p class="text-lg font-bold text-teal-600">2</p>
                <p class="text-xs text-slate-400">Soins</p>
              </div>
              <div class="bg-slate-50 rounded-xl p-3 text-center">
                <p class="text-lg font-bold text-green-600">✓</p>
                <p class="text-xs text-slate-400">Payé</p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- FEATURES -->
    <section id="features" class="py-20 bg-white">
      <div class="max-w-6xl mx-auto px-6">
        <div class="text-center mb-14">
          <h2 class="text-3xl font-bold text-slate-900 mb-3">Tout ce dont vous avez besoin</h2>
          <p class="text-slate-500 max-w-xl mx-auto">Une plateforme complète pour patients, médecins et équipes administratives.</p>
        </div>
        <div class="grid grid-cols-1 md:grid-cols-3 gap-6">
          <div v-for="feature in features" :key="feature.title" class="card-hover bg-slate-50 rounded-2xl p-6 border border-slate-100">
            <div class="w-12 h-12 rounded-xl flex items-center justify-center text-2xl mb-4" :class="feature.bg">{{ feature.icon }}</div>
            <h3 class="font-semibold text-slate-900 mb-2">{{ feature.title }}</h3>
            <p class="text-slate-500 text-sm leading-relaxed">{{ feature.desc }}</p>
          </div>
        </div>
      </div>
    </section>

    <!-- NOS MÉDECINS -->
    <section id="medecins" class="py-20 bg-slate-50">
      <div class="max-w-6xl mx-auto px-6">
        <div class="text-center mb-14">
          <span class="inline-block bg-brand-50 border border-brand-200 text-brand-700 text-xs font-semibold px-4 py-1.5 rounded-full mb-4">Notre équipe</span>
          <h2 class="text-3xl font-bold text-slate-900 mb-3">Des médecins qualifiés</h2>
          <p class="text-slate-500 max-w-xl mx-auto">Des professionnels de santé expérimentés dans toutes les spécialités médicales.</p>
        </div>
        <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
          <div v-for="doc in doctors" :key="doc.name" class="doc-card bg-white rounded-2xl border border-slate-200 overflow-hidden shadow-sm card-hover relative">
            <div class="relative">
              <img :src="doc.photo" :alt="doc.name" class="w-full h-52 object-cover object-top"/>
              <div class="doc-overlay absolute inset-0 bg-brand-600/80 flex items-center justify-center">
                <RouterLink to="/register" class="bg-white text-brand-700 font-semibold text-sm px-5 py-2.5 rounded-xl hover:bg-brand-50 transition">
                  Prendre RDV
                </RouterLink>
              </div>
              <span class="absolute top-3 right-3 bg-white text-xs font-semibold text-slate-700 px-2.5 py-1 rounded-full shadow-sm border border-slate-200">{{ doc.specialty }}</span>
            </div>
            <div class="p-5">
              <h3 class="font-bold text-slate-900 text-base">{{ doc.name }}</h3>
              <p class="text-slate-500 text-sm mt-0.5">{{ doc.role }}</p>
              <div class="flex items-center gap-2 mt-3">
                <div class="stars text-sm"></div>
                <span class="text-slate-500 text-xs font-medium">{{ doc.rating }} ({{ doc.reviews }} avis)</span>
              </div>
            </div>
          </div>
        </div>
        <div class="text-center mt-10">
          <RouterLink to="/register" class="inline-flex items-center gap-2 px-6 py-3 bg-white border border-slate-200 text-slate-700 font-semibold rounded-xl hover:border-brand-300 hover:text-brand-600 shadow-sm transition text-sm">
            Voir tous nos médecins
            <svg class="w-4 h-4" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" d="M17 8l4 4m0 0l-4 4m4-4H3"/>
            </svg>
          </RouterLink>
        </div>
      </div>
    </section>

    <!-- SPÉCIALITÉS -->
    <section id="specialites" class="py-20 bg-white">
      <div class="max-w-6xl mx-auto px-6">
        <div class="text-center mb-12">
          <h2 class="text-3xl font-bold text-slate-900 mb-3">Nos spécialités</h2>
          <p class="text-slate-500">Des médecins qualifiés dans toutes les disciplines médicales.</p>
        </div>
        <div class="flex flex-wrap justify-center gap-3">
          <span v-for="s in specialties" :key="s" class="bg-slate-50 border border-slate-200 text-slate-700 font-medium text-sm px-5 py-2.5 rounded-full shadow-sm hover:border-brand-300 hover:text-brand-600 cursor-pointer transition">{{ s }}</span>
        </div>
      </div>
    </section>

    <!-- TÉMOIGNAGES -->
    <section class="py-20 bg-slate-50">
      <div class="max-w-6xl mx-auto px-6">
        <div class="text-center mb-12">
          <h2 class="text-3xl font-bold text-slate-900 mb-3">Ce que disent nos patients</h2>
          <p class="text-slate-500">Des milliers de patients font confiance à AppSanté.</p>
        </div>
        <div class="grid grid-cols-1 md:grid-cols-3 gap-6">
          <div v-for="t in testimonials" :key="t.name" class="bg-white rounded-2xl p-6 border border-slate-200 shadow-sm card-hover">
            <div class="flex items-center gap-1 text-amber-400 text-sm mb-4">★★★★★</div>
            <p class="text-slate-600 text-sm leading-relaxed mb-5">"{{ t.text }}"</p>
            <div class="flex items-center gap-3">
              <img :src="t.photo" :alt="t.name" class="w-10 h-10 rounded-full object-cover border-2 border-slate-100"/>
              <div>
                <p class="font-semibold text-slate-800 text-sm">{{ t.name }}</p>
                <p class="text-slate-400 text-xs">{{ t.city }}</p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- CTA -->
    <section class="py-20 bg-brand-600">
      <div class="max-w-2xl mx-auto px-6 text-center">
        <h2 class="text-3xl font-bold text-white mb-4">Prêt à prendre soin de votre santé ?</h2>
        <p class="text-brand-200 mb-8">Inscription gratuite en moins de 2 minutes. Aucune carte de crédit requise.</p>
        <RouterLink to="/register" class="inline-block px-8 py-4 bg-white text-brand-700 font-bold rounded-xl hover:bg-brand-50 shadow-lg transition">
          Créer mon compte patient
        </RouterLink>
      </div>
    </section>

    <!-- FOOTER -->
    <footer class="bg-slate-900 text-slate-400 py-8">
      <div class="max-w-6xl mx-auto px-6 flex flex-col md:flex-row items-center justify-between gap-4">
        <div class="flex items-center gap-2">
          <div class="w-7 h-7 bg-brand-600 rounded-lg flex items-center justify-center">
            <svg class="w-4 h-4 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2.5" d="M4.5 12h15m-7.5-7.5v15"/>
            </svg>
          </div>
          <span class="text-white font-semibold">AppSanté</span>
        </div>
        <p class="text-sm">© 2026 AppSanté — Tous droits réservés</p>
        <div class="flex gap-4 text-sm">
          <a href="#" class="hover:text-white transition">Confidentialité</a>
          <a href="#" class="hover:text-white transition">Conditions</a>
        </div>
      </div>
    </footer>

  </div>
</template>

<script setup>
const features = [
  { icon: '📅', bg: 'bg-brand-100', title: 'Rendez-vous en ligne',   desc: 'Réservez selon les disponibilités en temps réel. Confirmation instantanée par email.' },
  { icon: '🗂️', bg: 'bg-teal-100',  title: 'Dossier médical',        desc: 'Soins, ordonnances, radiographies et analyses — tout centralisé et sécurisé.' },
  { icon: '🧾', bg: 'bg-green-100', title: 'Facturation claire',      desc: 'Suivez vos factures et paiements. Historique complet à portée de main.' },
  { icon: '🔔', bg: 'bg-purple-100',title: 'Rappels automatiques',    desc: 'Email de rappel J-1 avant chaque rendez-vous. Ne ratez plus jamais une consultation.' },
  { icon: '💊', bg: 'bg-orange-100',title: 'Ordonnances',             desc: 'Accédez à vos ordonnances et traitements à tout moment depuis votre espace.' },
  { icon: '🔐', bg: 'bg-red-100',   title: 'Sécurité maximale',       desc: 'Authentification JWT, mots de passe chiffrés BCrypt, accès par rôle strict.' }
]

const doctors = [
  { name: 'Dr. Hassan Benali',    role: 'Cardiologue · 14 ans d\'expérience',       specialty: '❤️ Cardiologie',       photo: 'https://randomuser.me/api/portraits/men/52.jpg',   rating: '4.9', reviews: 127 },
  { name: 'Dr. Amina Bouziane',   role: 'Pédiatre · 9 ans d\'expérience',           specialty: '🧸 Pédiatrie',          photo: 'https://randomuser.me/api/portraits/women/44.jpg', rating: '4.8', reviews: 94  },
  { name: 'Dr. Youssef Rachid',   role: 'Ophtalmologue · 11 ans d\'expérience',     specialty: '👁️ Ophtalmologie',      photo: 'https://randomuser.me/api/portraits/men/15.jpg',   rating: '4.7', reviews: 83  },
  { name: 'Dr. Fatima Ait Brahim',role: 'Neurologue · 16 ans d\'expérience',        specialty: '🧠 Neurologie',         photo: 'https://randomuser.me/api/portraits/women/28.jpg', rating: '5.0', reviews: 211 },
  { name: 'Dr. Karim Nassiri',    role: 'Orthopédiste · 12 ans d\'expérience',      specialty: '🦴 Orthopédie',         photo: 'https://randomuser.me/api/portraits/men/67.jpg',   rating: '4.8', reviews: 156 },
  { name: 'Dr. Leila Berrada',    role: 'Médecin généraliste · 8 ans d\'expérience',specialty: '🩺 Médecine générale',  photo: 'https://randomuser.me/api/portraits/women/67.jpg', rating: '4.9', reviews: 178 }
]

const specialties = ['🦷 Dentaire', '❤️ Cardiologie', '👁️ Ophtalmologie', '👂 ORL', '🧸 Pédiatrie', '🧠 Neurologie', '🦴 Orthopédie', '🩺 Médecine générale']

const testimonials = [
  { name: 'Yasmine El Fassi', city: 'Casablanca', photo: 'https://randomuser.me/api/portraits/women/12.jpg', text: 'Application très intuitive. J\'ai pu prendre un rendez-vous en moins de 2 minutes et j\'ai reçu un rappel la veille. Excellent service !' },
  { name: 'Omar Benkirane',   city: 'Rabat',       photo: 'https://randomuser.me/api/portraits/men/22.jpg',   text: 'Mon dossier médical est enfin organisé. Je peux accéder à mes ordonnances depuis mon téléphone. Je recommande vivement !' },
  { name: 'Dr. Sara Tazi',    city: 'Marrakech',   photo: 'https://randomuser.me/api/portraits/women/55.jpg', text: 'En tant que médecin, cette plateforme m\'aide à gérer mes rendez-vous efficacement. Le tableau de bord est très clair.' }
]
</script>
