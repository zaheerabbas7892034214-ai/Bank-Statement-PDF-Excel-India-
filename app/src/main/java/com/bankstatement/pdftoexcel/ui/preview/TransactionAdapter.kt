package com.bankstatement.pdftoexcel.ui.preview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bankstatement.pdftoexcel.R
import com.bankstatement.pdftoexcel.data.BankTransaction

class TransactionAdapter(
    private val transactions: List<BankTransaction>,
    private val isPremium: Boolean,
    private val freeRowsLimit: Int = 10
) : RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {
    
    class TransactionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dateText: TextView = itemView.findViewById(R.id.dateText)
        val descriptionText: TextView = itemView.findViewById(R.id.descriptionText)
        val debitText: TextView = itemView.findViewById(R.id.debitText)
        val creditText: TextView = itemView.findViewById(R.id.creditText)
        val balanceText: TextView = itemView.findViewById(R.id.balanceText)
        val blurOverlay: View = itemView.findViewById(R.id.blurOverlay)
        val contentLayout: View = itemView.findViewById(R.id.contentLayout)
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_transaction, parent, false)
        return TransactionViewHolder(view)
    }
    
    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val transaction = transactions[position]
        val isLocked = !isPremium && position >= freeRowsLimit
        
        holder.dateText.text = transaction.date
        holder.descriptionText.text = transaction.description
        holder.debitText.text = if (transaction.debit.isNotEmpty()) "₹${transaction.debit}" else "-"
        holder.creditText.text = if (transaction.credit.isNotEmpty()) "₹${transaction.credit}" else "-"
        holder.balanceText.text = if (transaction.balance.isNotEmpty()) "₹${transaction.balance}" else "-"
        
        // Apply blur/lock effect
        if (isLocked) {
            holder.blurOverlay.visibility = View.VISIBLE
            holder.contentLayout.alpha = 0.5f
        } else {
            holder.blurOverlay.visibility = View.GONE
            holder.contentLayout.alpha = 1.0f
        }
    }
    
    override fun getItemCount(): Int = transactions.size
}
