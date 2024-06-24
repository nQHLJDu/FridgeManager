package com.example.FridgeManager

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.DatePicker
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.FridgeManager.databinding.ActivitySelectImageBinding
import java.text.SimpleDateFormat
import java.util.*

class SelectImageActivity : AppCompatActivity() , DatePickerDialog.OnDateSetListener{
    private lateinit var binding: ActivitySelectImageBinding
    private lateinit var expiryDateEditText: EditText
    private val dateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.US)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectImageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ImageViewのIDと対応する画像リソースのペアをリストにまとめる
        val imageViewData = listOf(
            Triple(R.id.itemImageView1, R.drawable.kyabetsu, "キャベツ"),
            Triple(R.id.itemImageView2, R.drawable.tomato, "トマト"),
            Triple(R.id.itemImageView3, R.drawable.kyuri, "きゅうり"),
            Triple(R.id.itemImageView4, R.drawable.renkon, "れんこん"),
            Triple(R.id.itemImageView5, R.drawable.sanma, "サンマ"),
            Triple(R.id.itemImageView6, R.drawable.apple, "りんご"),
            // 他の画像ペアもここに追加
        )

        // ループで設定を行う
        imageViewData.forEach { (imageViewId, imageResId, imageName) ->
            findViewById<ImageView>(imageViewId).setOnClickListener {
                showInputDialog(imageResId, imageName)
            }
        }

        // 戻るボタン押下時
        binding.backButton.setOnClickListener {
            finish()
        }
    }

    private fun showInputDialog(imageResId: Int, imageName: String) {
        // カスタムレイアウトをインフレート
        val dialogView = layoutInflater.inflate(R.layout.dialog_input, null)
        expiryDateEditText = dialogView.findViewById(R.id.expiryDateEditText)
        val numberEditText = dialogView.findViewById<EditText>(R.id.numberEditText)

        // ダイアログの作成
        val dialog = AlertDialog.Builder(this)
            .setTitle("商品情報入力")
            .setView(dialogView)
            .setPositiveButton("OK",null)  // nullを渡して、後でリスナーを設定
            .setNegativeButton("キャンセル", null)
            .create()

        dialog.setOnShowListener {
            val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            positiveButton.setOnClickListener {
                val numberText = numberEditText.text.toString()
                val expiryDateText = expiryDateEditText.text.toString()

                if (numberText.isEmpty() || expiryDateText.isEmpty()) {
                    // エラーメッセージを表示
                    if (numberText.isEmpty()) {
                        numberEditText.error = "数量を入力してください"
                    }
                    if (expiryDateText.isEmpty()) {
                        expiryDateEditText.error = "消費期限を入力してください"
                    }
                } else {
                    try {
                        val number = numberText.toInt()

                        // 入力が正しい場合はIntentにデータをセットしてActivityを終了
                        val intent = Intent()
                        intent.putExtra("selectedImage", imageResId)
                        intent.putExtra("name", imageName)
                        intent.putExtra("number", number)
                        intent.putExtra("expiryDate", expiryDateText)
                        setResult(RESULT_OK, intent)
                        dialog.dismiss()
                        finish()
                    }
                    // 数字はここでエラーキャッチ
                    catch (e: NumberFormatException) {
                        numberEditText.error = "数量には数値を入力してください"
                    }
                }
            }
        }

        // 消費期限項目の押下時処理
        expiryDateEditText.setOnClickListener {
            expiryDateEditText.showDatePickerFragment(supportFragmentManager, dateFormat)
        }

        dialog.show()
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance().apply {
            set(year, month, dayOfMonth)
        }
        expiryDateEditText.setText(dateFormat.format(calendar.time))
    }

}
