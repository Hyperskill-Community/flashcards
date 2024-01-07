// https://router.vuejs.org/guide/essentials/nested-routes.html#nested-routes
const routes = [
  {
    path: '/',
    name: 'Home',
    component: () => import('@/shared/pages/Home.vue'),
  },
  {
    path: '/display/:id',
    name: 'card-details',
    component: () => import("@/feature/cardmanagement/pages/CardDetailsPage.vue"),
    props: true,
  },
  // Always leave this as last one,
  // but you can also remove it
  {
    path: '/:catchAll(.*)*',
    component: () => import('@/shared/pages/ErrorNotFound.vue'),
  }
]

export default routes
