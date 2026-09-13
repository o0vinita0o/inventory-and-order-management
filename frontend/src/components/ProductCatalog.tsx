import React from 'react';
import type { Product } from '../types';

export const ProductCatalog: React.FC<{ products: Product[] }> = ({ products }) => {
    return (
        <div className="p-6 border border-slate-800 rounded-xl shadow-sm bg-slate-900">
            <h3 className="text-lg font-bold mb-4 text-white">Current Catalog</h3>
            {(!products || products.length === 0) ? (
                <p className="text-slate-400">No products available.</p>
            ) : (
                <ul className="space-y-3">
                    {products.map(p => (
                        <li key={p.id} className="flex justify-between items-center p-3 bg-slate-800 rounded">
                            <span className="font-medium text-slate-200">{p.name}</span>
                            <div className="text-right">
                                <div className="text-emerald-400 font-bold">${p.price}</div>
                                <div className="text-sm text-slate-400">Stock: {p.stockQuantity}</div>
                            </div>
                        </li>
                    ))}
                </ul>
            )}
        </div>
    );
};