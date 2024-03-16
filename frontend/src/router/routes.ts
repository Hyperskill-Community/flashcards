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
    component: () => import("@/feature/cards/pages/CardsPage.vue"),
    props: true,
  },
  {
    path: '/export',
    name: 'export',
    component: () => import("@/feature/dataio/pages/DataExport.vue"),
  },
  {
    path: '/register',
    name: 'register',
    component: () => import("@/feature/registration/pages/RegistrationPage.vue"),
  },
  // Always leave this as last one,
  // but you can also remove it
  {
    path: '/:catchAll(.*)*',
    component: () => import('@/shared/pages/ErrorNotFound.vue'),
  }
];

export default routes;
