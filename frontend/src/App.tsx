import { useQuery } from '@tanstack/react-query';
import { fetchProducts, fetchOrders } from './services/api';
import { ProductCatalog } from './components/ProductCatalog';
import OrderForm from './components/OrderForm';
import { OrderHistoryTable } from './components/OrderHistoryTable';
import type { Product, OrderResponse } from './types';

export default function App() {
  const { data: products = [] } = useQuery<Product[]>({ 
      queryKey: ['products'], 
      queryFn: () => fetchProducts()
  });
  
  const { data: orders = [] } = useQuery<OrderResponse[]>({ 
      queryKey: ['orders'], 
      queryFn: () => fetchOrders()
  });

  return (
    <div className="min-h-screen bg-slate-950 text-slate-100 p-8 font-sans">
      <div className="max-w-5xl mx-auto space-y-8">
        <div className="border-b border-slate-800 pb-4">
          <h1 className="text-3xl font-bold tracking-tight text-white">Inventory & Order Management</h1>
          <p className="text-slate-400 text-sm mt-1">Manage stock levels and execute transactions synchronously</p>
        </div>
        <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
          <ProductCatalog products={products} />
          <OrderForm products={products} />
        </div>
        <OrderHistoryTable orders={orders} />
      </div>
    </div>
  );
}