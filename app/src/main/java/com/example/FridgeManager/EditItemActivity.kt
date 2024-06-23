package com.example.FridgeManager

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.FridgeManager.databinding.ActivityEditItemBinding
import java.text.SimpleDateFormat
import java.util.*

class EditItemActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditItemBinding
    private val dateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.US)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditItemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Intentから商品情報を取得
        val item: FridgeItem? = intent.getParcelableExtra("item")

        // 取得したアイテムの情報をUIに表示
        item?.let {
            binding.itemImageView.setImageResource(it.imageResId)
            binding.numberEditText.setText(it.number.toString())
            binding.expiryDateEditText.setText(it.expiryDate)
        }

        // 消費期限項目の押下時処理
        binding.expiryDateEditText.setOnClickListener {
            binding.expiryDateEditText.showDatePickerFragment(supportFragmentManager, dateFormat)
        }

        // 保存ボタンのクリックリスナー
        binding.saveButton.setOnClickListener {
            // 入力した情報を取得
            val updatedNumber = binding.numberEditText.text.toString().toIntOrNull() ?: 0
            val updatedExpiryDate = binding.expiryDateEditText.text.toString()

            val updatedItem = item?.copy(number = updatedNumber, expiryDate = updatedExpiryDate)

            // 修正した情報をMainActivityに返す
            val updateIntent = Intent().apply {
                putExtra("updatedItem", updatedItem)
            }
            setResult(Activity.RESULT_OK, updateIntent)
            finish()
        }

        // 削除ボタンの処理
        binding.deleteButton.setOnClickListener {
            // 削除した情報をMainActivityに返す
            val deleteIntent = Intent().apply {
                putExtra("deleteItem", item)
            }
            setResult(Activity.RESULT_OK, deleteIntent)
            finish()
        }

        // 閉じるボタンの処理
        binding.closeButton.setOnClickListener {
            // メイン画面に遷移
            finish()
        }
    }
}
