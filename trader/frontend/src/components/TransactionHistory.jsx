import React from 'react';

function TransactionHistory({ transactions }) {
  return (
    <div className="transactions-container">
      <h2>Transaction History</h2>
      {transactions.length === 0 ? (
        <p className="empty-state">No transactions yet.</p>
      ) : (
        <table className="stock-table">
          <thead>
            <tr>
              <th>Type</th>
              <th>Stock</th>
              <th>Qty</th>
              <th>Price/Share</th>
              <th>Total</th>
              <th>Time</th>
            </tr>
          </thead>
          <tbody>
            {transactions.map((tx, i) => (
              <tr key={i} className={tx.type === 'BUY' ? 'buy-row' : 'sell-row'}>
                <td><span className={`badge ${tx.type.toLowerCase()}`}>{tx.type}</span></td>
                <td className="symbol">{tx.stockSymbol}</td>
                <td>{tx.quantity}</td>
                <td>${Number(tx.pricePerShare).toFixed(2)}</td>
                <td>${Number(tx.totalAmount).toFixed(2)}</td>
                <td>{new Date(tx.timestamp).toLocaleString()}</td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
}

export default TransactionHistory;
