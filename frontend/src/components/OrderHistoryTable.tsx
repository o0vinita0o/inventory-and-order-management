import { formatCurrency } from '../types';
import type { OrderResponse } from '../types';

export function OrderHistoryTable({ orders }: { orders: OrderResponse[] }) {
  if (orders.length === 0) return null;

  return (
    <div className="bg-slate-900 border border-slate-800 rounded-xl p-6 shadow-xl">
      <h2 className="text-xl font-semibold mb-4 text-slate-200">Order History</h2>
      <div className="overflow-x-auto">
        <table className="w-full text-left text-sm text-slate-300">
          <thead className="text-xs uppercase bg-slate-800/60 text-slate-400 border-b border-slate-700">
            <tr>
              <th className="px-4 py-3">Order ID</th>
              <th className="px-4 py-3">Date</th>
              <th className="px-4 py-3">Items</th>
              <th className="px-4 py-3">Total</th>
              <th className="px-4 py-3">Status</th>
            </tr>
          </thead>
          <tbody className="divide-y divide-slate-800">
            {orders.map((o) => (
              <tr key={o.orderId} className="hover:bg-slate-800/30">
                <td className="px-4 py-3 font-semibold text-white">#{o.orderId}</td>
                <td className="px-4 py-3 text-slate-400">{new Date(o.orderDate).toLocaleString()}</td>
                <td className="px-4 py-3">
                  {o.items.map((i) => <div key={i.productId}>{i.productName} × {i.quantity}</div>)}
                </td>
                <td className="px-4 py-3 font-medium text-emerald-400">{formatCurrency(o.totalAmount)}</td>
                <td className="px-4 py-3"><span className="px-2 py-0.5 text-xs rounded bg-slate-800 border border-slate-700">{o.status}</span></td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}