import { createRouter, createWebHistory } from 'vue-router'
import Home from '../pages/Home.vue'
import CategoryPage from '../pages/CategoryPage.vue'
import ProductPage from '../pages/ProductPage.vue'
import ContactPage from '../pages/ContactPage.vue'
import AboutPage from '../pages/AboutPage.vue'
import InquiryPage from '../pages/InquiryPage.vue'
import InquirySuccessPage from '../pages/InquirySuccessPage.vue'
import AdminInquiriesPage from '../pages/AdminInquiriesPage.vue'
import AdminLoginPage from '../pages/AdminLoginPage.vue'
import AdminCatalogPage from '../pages/AdminCatalogPage.vue'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    { path: '/', component: Home },
    { path: '/categories/:categoryId', component: CategoryPage, props: true },
    { path: '/products/:productId', component: ProductPage, props: true },
    { path: '/contact', component: ContactPage },
    { path: '/about', component: AboutPage },
    { path: '/inquiry', component: InquiryPage },
    { path: '/inquiry/success', component: InquirySuccessPage },
    { path: '/admin/login', component: AdminLoginPage },
    { path: '/admin/catalog', component: AdminCatalogPage },
    { path: '/admin/inquiries', component: AdminInquiriesPage }
  ]
})

export default router

