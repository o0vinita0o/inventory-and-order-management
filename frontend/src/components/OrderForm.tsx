import React, { useState } from 'react';
import { useMutation, useQueryClient } from '@tanstack/react-query';
import { placeOrder } from '../services/api';
import type { Product, OrderRequest, OrderResponse } from '../types';
import axios from 'axios';

export default function OrderForm({ products }: { products: Product[] }) {
    const [productId, setProductId] = useState<number | ''>('');
    const [quantity, setQuantity] = useState<number | ''>('');
    const [status, setStatus] = useState<{ error: string | null; success: OrderResponse | null }>({
        error: null,
        success: null
    });

    const queryClient = useQueryClient();

    const orderMutation = useMutation({
        mutationFn: (newOrder: OrderRequest) => placeOrder(newOrder),
        onSuccess: (data) => {
            setStatus({ error: null, success: data });
            setProductId('');
            setQuantity('');
            queryClient.invalidateQueries({ queryKey: ['products'] });
            queryClient.invalidateQueries({ queryKey: ['orders'] });
        },
        onError: (error) => {
            let errorMessage = 'Failed to place order due to insufficient stock.';
            if (axios.isAxiosError(error)) {
                errorMessage = error.response?.data?.message || error.response?.data?.error || errorMessage;
            }
            setStatus({ error: errorMessage, success: null });
        }
    });

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();
        if (!productId || !quantity) return;
        setStatus({ error: null, success: null });
        orderMutation.mutate({ productId: Number(productId), quantity: Number(quantity) });
    };

    return (
        <form onSubmit={handleSubmit} className="p-6 border border-slate-800 rounded-xl shadow-sm bg-slate-900">
            <h3 className="text-lg font-bold mb-4 text-white">Place New Order</h3>
            
            <div className="mb-4">
                <label className="block mb-1 font-medium text-slate-300">Product</label>
                <select 
                    value={productId} 
                    onChange={(e) => setProductId(Number(e.target.value))} 
                    className="border border-slate-700 bg-slate-800 text-white p-2 w-full rounded focus:ring-2 focus:ring-blue-500 outline-none"
                    required 
                >
                    <option value="" disabled>Select a product...</option>
                    {products.map(p => (
                        <option key={p.id} value={p.id}>{p.name} (${p.price})</option>
                    ))}
                </select>
            </div>

            <div className="mb-4">
                <label className="block mb-1 font-medium text-slate-300">Quantity</label>
                <input 
                    type="number" 
                    min="1"
                    value={quantity} 
                    onChange={(e) => setQuantity(Number(e.target.value))} 
                    className="border border-slate-700 bg-slate-800 text-white p-2 w-full rounded focus:ring-2 focus:ring-blue-500 outline-none"
                    required 
                />
            </div>

            <button 
                type="submit" 
                disabled={orderMutation.isPending}
                className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700 disabled:opacity-50 transition-colors w-full"
            >
                {orderMutation.isPending ? 'Processing...' : 'Submit Order'}
            </button>

            {status.error && (
                <div className="mt-4 p-3 bg-red-900/50 border border-red-800 text-red-200 rounded font-medium text-sm">
                    {status.error}
                </div>
            )}

            {status.success && (
                <div className="mt-4 p-3 bg-emerald-900/50 border border-emerald-800 text-emerald-200 rounded font-medium text-sm">
                    Order is {status.success.status}! Total: ${status.success.totalAmount}
                </div>
            )}
        </form>
    );
}