import { createRouter, createWebHistory } from 'vue-router'
import Home from '../pages/Home.vue'
import CategoryPage from '../pages/CategoryPage.vue'
import ProductPage from '../pages/ProductPage.vue'
import ContactPage from '../pages/ContactPage.vue'
import InquiryPage from '../pages/InquiryPage.vue'
import AdminInquiriesPage from '../pages/AdminInquiriesPage.vue'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    { path: '/', component: Home },
    { path: '/categories/:categoryId', component: CategoryPage, props: true },
    { path: '/products/:productId', component: ProductPage, props: true },
    { path: '/contact', component: ContactPage },
    { path: '/inquiry', component: InquiryPage },
    { path: '/admin/inquiries', component: AdminInquiriesPage }
  ]
})

export default router

