import axios from 'axios';
import type { Product, OrderRequest, OrderResponse } from '../types';

const API_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api';

export const fetchProducts = async (): Promise<Product[]> => {
    const response = await axios.get<Product[]>(`${API_URL}/products`);
    return response.data;
};

export const fetchOrders = async (): Promise<OrderResponse[]> => {
    const response = await axios.get<OrderResponse[]>(`${API_URL}/orders`);
    return response.data;
};

export const placeOrder = async (orderRequest: OrderRequest): Promise<OrderResponse> => {
    const response = await axios.post<OrderResponse>(`${API_URL}/orders`, orderRequest);
    return response.data;
};