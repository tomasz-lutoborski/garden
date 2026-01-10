(ns garden.views.layout
  "Base HTML layout and shared components."
  (:require [hiccup2.core :as h]))

(def tailwind-config
  "tailwind.config = {
    darkMode: 'class',
    theme: {
      extend: {
        fontFamily: {
          mono: ['Geist Mono', 'monospace'],
          body: ['Raleway', 'sans-serif'],
        },
        colors: {
          surface: {
            light: '#faf6f3',
            dark: '#1a1520',
          },
          ink: {
            light: '#4a4544',
            dark: '#e5e0db',
          },
          muted: {
            light: '#9a918c',
            dark: '#7a7080',
          },
          accent: {
            light: '#c27878',
            dark: '#e8a5a5',
          },
          link: {
            light: '#c48c6c',
            dark: '#e8b899',
          },
          hover: {
            light: '#a86060',
            dark: '#f0c0b0',
          },
        }
      }
    }
  }")

(def theme-init-script
  "if (localStorage.theme === 'dark' || (!('theme' in localStorage) && window.matchMedia('(prefers-color-scheme: dark)').matches)) {
    document.documentElement.classList.add('dark')
  } else {
    document.documentElement.classList.remove('dark')
  }")

(def theme-toggle-script
  "function toggleTheme() {
    if (document.documentElement.classList.contains('dark')) {
      document.documentElement.classList.remove('dark');
      localStorage.theme = 'light';
    } else {
      document.documentElement.classList.add('dark');
      localStorage.theme = 'dark';
    }
  }")

(defn head
  "Render the <head> section."
  [title]
  [:head
   [:meta {:charset "utf-8"}]
   [:meta {:name "viewport" :content "width=device-width, initial-scale=1"}]
   [:title title]
   [:link {:rel "preconnect" :href "https://fonts.googleapis.com"}]
   [:link {:rel "preconnect" :href "https://fonts.gstatic.com" :crossorigin true}]
   [:link {:href "https://fonts.googleapis.com/css2?family=Raleway:wght@400;500;600&display=swap" :rel "stylesheet"}]
   [:link {:href "https://cdn.jsdelivr.net/npm/geist@1.3.1/dist/fonts/geist-mono/style.min.css" :rel "stylesheet"}]
   [:script {:src "https://cdn.tailwindcss.com?plugins=typography"}]
   [:script (h/raw tailwind-config)]
   [:script (h/raw theme-init-script)]])

(defn nav
  "Render the navigation bar."
  []
  [:nav.mb-16.flex.justify-between.items-center
   [:a.text-link-light.dark:text-link-dark.hover:text-hover-light.dark:hover:text-hover-dark.transition-colors.font-mono.text-base.tracking-wide
    {:href "/"} "home"]
   [:button#theme-toggle.font-mono.text-sm.tracking-wide.px-4.py-1.5.rounded-full.border.border-muted-light.dark:border-muted-dark.text-muted-light.dark:text-muted-dark.hover:text-accent-light.dark:hover:text-accent-dark.hover:border-accent-light.dark:hover:border-accent-dark.transition-colors
    {:onclick "toggleTheme()"} "theme"]])

(defn layout
  "Wrap content in the base HTML layout."
  [title & body]
  (str
   (h/html
    [:html
     (head title)
     [:body.min-h-screen.font-body.bg-surface-light.text-ink-light.dark:bg-surface-dark.dark:text-ink-dark.transition-colors.duration-200.text-lg.leading-relaxed.antialiased
      [:div.max-w-2xl.mx-auto.px-6.py-12.sm:py-16
       (nav)
       body]
      [:script (h/raw theme-toggle-script)]]])))
