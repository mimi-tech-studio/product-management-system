import { createStore } from 'vuex'

export default createStore({
  state: {
    products: [],
    categories: [],
    loading: false,
    user: null
  },
  mutations: {
    SET_PRODUCTS(state, products) {
      state.products = products
    },
    SET_CATEGORIES(state, categories) {
      state.categories = categories
    },
    SET_LOADING(state, loading) {
      state.loading = loading
    },
    SET_USER(state, user) {
      state.user = user
    },
    ADD_PRODUCT(state, product) {
      state.products.push(product)
    },
    UPDATE_PRODUCT(state, updatedProduct) {
      const index = state.products.findIndex(p => p.id === updatedProduct.id)
      if (index !== -1) {
        state.products.splice(index, 1, updatedProduct)
      }
    },
    DELETE_PRODUCT(state, productId) {
      state.products = state.products.filter(p => p.id !== productId)
    }
  },
  actions: {
    async fetchProducts({ commit }) {
      commit('SET_LOADING', true)
      try {
        // 这里应该调用API，暂时使用模拟数据
        const mockProducts = [
          { id: 1, name: '笔记本电脑', price: 5999.99, stock: 50, category: '电子产品' },
          { id: 2, name: '智能手机', price: 3999.99, stock: 100, category: '电子产品' },
          { id: 3, name: '办公椅', price: 899.99, stock: 20, category: '办公家具' }
        ]
        commit('SET_PRODUCTS', mockProducts)
      } catch (error) {
        console.error('获取产品失败:', error)
      } finally {
        commit('SET_LOADING', false)
      }
    },
    async fetchCategories({ commit }) {
      try {
        // 模拟数据
        const categories = ['电子产品', '办公家具', '图书', '家电']
        commit('SET_CATEGORIES', categories)
      } catch (error) {
        console.error('获取分类失败:', error)
      }
    }
  },
  getters: {
    allProducts: state => state.products,
    productCount: state => state.products.length,
    activeProducts: state => state.products.filter(p => p.isActive !== false),
    categories: state => state.categories,
    isLoading: state => state.loading,
    currentUser: state => state.user
  }
})
