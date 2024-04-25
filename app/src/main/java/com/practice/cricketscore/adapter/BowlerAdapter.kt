package com.practice.cricketscore.adapter

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputLayout
import com.practice.cricketscore.databinding.ItemPlayerBinding
import com.practice.cricketscore.databinding.NameDialogBinding
import com.practice.cricketscore.models.Bowler

class BowlerAdapter(
    private val context: Context,
    private val list: List<Bowler>,
) :
    RecyclerView.Adapter<BowlerAdapter.ViewHolder>() {

    private var onItemClickListener: OnItemClickListener? = null


    inner class ViewHolder(val binding: ItemPlayerBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPlayerBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.playerNameLayout.setOnClickListener {
            dialogShow(position)
        }
        holder.binding.playerName.text = list[position].name
        holder.binding.playerImg.setOnClickListener {
            onItemClickListener?.onClick(position)
        }
    }

    private fun dialogShow(position: Int) {
        val dialog = Dialog(context)
        val dialogBinding = NameDialogBinding.inflate(LayoutInflater.from(context))
        dialog.setContentView(dialogBinding.root)

        dialogBinding.userNameEt.setText(list[position].name)
        dialogBinding.submitButton.setOnClickListener {
            if (dialogBinding.userNameEt.text.isNullOrEmpty()) {
                dialogBinding.userNameTil.setErrorMessage("Name can not be empty!")
            } else {
                val name = dialogBinding.userNameEt.text.toString().trim()
                list[position].name = name
                dialog.dismiss()
                notifyItemChanged(position)
            }
        }
        dialogBinding.cancelButton.setOnClickListener {
            dialog.dismiss()
        }
        dialogBinding.userNameEt.doOnTextChanged { _, _, _, _ ->
            dialogBinding.userNameTil.setErrorMessage("")
        }
        dialog.show()
    }

    fun setOnClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    interface OnItemClickListener {
        fun onClick(position: Int)
    }

    private fun TextInputLayout.setErrorMessage(message: String) {
        error = message
        isErrorEnabled = message.isNotEmpty()
    }

}