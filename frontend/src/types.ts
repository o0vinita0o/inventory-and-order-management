export interface Product {
    id: number;
    name: string;
    price: number;
    stockQuantity: number;
}

export interface OrderItemResponse {
    productId: number;
    productName: string;
    quantity: number;
    priceAtPurchase: number;
}

export interface OrderResponse {
    orderId: number;
    orderDate: string;
    status: string;
    totalAmount: number;
    items: OrderItemResponse[];
}

export interface OrderRequest {
    productId: number;
    quantity: number;
}

export const formatCurrency = (amount: number) => {
    return new Intl.NumberFormat('en-US', {
        style: 'currency',
        currency: 'USD',
        minimumFractionDigits: 2
    }).format(amount);
};