// https://router.vuejs.org/guide/essentials/nested-routes.html#nested-routes
const routes = [
  {
    path: '/',
    name: 'welcome',
    component: () => import('@/shared/pages/Welcome.vue'),
  },
  {
    path: '/categories',
    name: 'categories',
    component: () => import("@/feature/category/pages/CategoryOverviewPage.vue"),
  },
  {
    path: '/category/:categoryId',
    name: 'cards-by-category',
    component: () => import("@/feature/cards/pages/CardOverviewPage.vue"),
    props: true,
  },
  {
    path: '/card/:categoryId/:cardId',
    name: 'card-details',
    component: () => import("@/feature/cards/pages/CardDetailsPage.vue"),
    props: true,
  },
  {
    path: '/register',
    name: 'axios-line-temp',
    component: () => import("@/feature/registration/pages/RegistrationPage.vue"),
  },
  // Always leave this as last one,
  // but you can also remove it
  {
    path: '/:catchAll(.*)*',
    component: () => import('@/shared/pages/ErrorNotFound.vue'),
  }
]

export default routes
