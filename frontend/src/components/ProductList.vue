<template>
  <div class="product-list">
    <h2>产品列表</h2>
    
    <el-button type="primary" @click="showAddDialog = true">添加产品</el-button>
    
    <el-table :data="products" style="width: 100%" v-loading="loading">
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="name" label="产品名称" />
      <el-table-column prop="category" label="分类" />
      <el-table-column prop="price" label="价格" />
      <el-table-column prop="stock" label="库存" />
      <el-table-column prop="sku" label="SKU" />
      <el-table-column label="操作" width="200">
        <template #default="scope">
          <el-button size="small" @click="editProduct(scope.row)">编辑</el-button>
          <el-button size="small" type="danger" @click="deleteProduct(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    
    <!-- 添加/编辑对话框 -->
    <el-dialog v-model="showAddDialog" :title="isEditing ? '编辑产品' : '添加产品'">
      <el-form :model="currentProduct" label-width="80px">
        <el-form-item label="产品名称">
          <el-input v-model="currentProduct.name" />
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="currentProduct.description" type="textarea" />
        </el-form-item>
        <el-form-item label="价格">
          <el-input-number v-model="currentProduct.price" :min="0" :precision="2" />
        </el-form-item>
        <el-form-item label="库存">
          <el-input-number v-model="currentProduct.stock" :min="0" />
        </el-form-item>
        <el-form-item label="分类">
          <el-input v-model="currentProduct.category" />
        </el-form-item>
        <el-form-item label="SKU">
          <el-input v-model="currentProduct.sku" />
        </el-form-item>
      </el-form>
      
      <template #footer>
        <el-button @click="showAddDialog = false">取消</el-button>
        <el-button type="primary" @click="saveProduct">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue'
import axios from 'axios'

export default {
  name: 'ProductList',
  setup() {
    const products = ref([])
    const loading = ref(false)
    const showAddDialog = ref(false)
    const isEditing = ref(false)
    const currentProduct = ref({
      name: '',
      description: '',
      price: 0,
      stock: 0,
      category: '',
      sku: ''
    })
    
    // 加载产品列表
    const loadProducts = async () => {
      loading.value = true
      try {
        const response = await axios.get('http://localhost:8082/api/products')
        products.value = response.data
      } catch (error) {
        console.error('加载产品失败:', error)
      } finally {
        loading.value = false
      }
    }
    
    // 编辑产品
    const editProduct = (product) => {
      currentProduct.value = { ...product }
      isEditing.value = true
      showAddDialog.value = true
    }
    
    // 删除产品
    const deleteProduct = async (id) => {
      try {
        await axios.delete(`http://localhost:8082/api/products/${id}`)
        await loadProducts()
      } catch (error) {
        console.error('删除产品失败:', error)
      }
    }
    
    // 保存产品
    const saveProduct = async () => {
      try {
        if (isEditing.value) {
          await axios.put(`http://localhost:8082/api/products/${currentProduct.value.id}`, currentProduct.value)
        } else {
          await axios.post('http://localhost:8082/api/products', currentProduct.value)
        }
        showAddDialog.value = false
        resetForm()
        await loadProducts()
      } catch (error) {
        console.error('保存产品失败:', error)
      }
    }
    
    // 重置表单
    const resetForm = () => {
      currentProduct.value = {
        name: '',
        description: '',
        price: 0,
        stock: 0,
        category: '',
        sku: ''
      }
      isEditing.value = false
    }
    
    onMounted(() => {
      loadProducts()
    })
    
    return {
      products,
      loading,
      showAddDialog,
      isEditing,
      currentProduct,
      editProduct,
      deleteProduct,
      saveProduct
    }
  }
}
</script>

<style scoped>
.product-list {
  padding: 20px;
}

.el-button {
  margin-bottom: 20px;
}
</style>
