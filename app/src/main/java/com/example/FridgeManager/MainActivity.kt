package com.example.FridgeManager

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.FridgeManager.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val fridgeItems = mutableListOf<FridgeItem>()
    private lateinit var fridgeItemAdapter: FridgeItemAdapter
    private val dateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.US)
    private val notificationChannelId = "expiry_notification_channel"
    private val notificationId = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // RecyclerViewの設定
        fridgeItemAdapter = FridgeItemAdapter(fridgeItems){ item -> editItem(item) }
        binding.itemsRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.itemsRecyclerView.adapter = fridgeItemAdapter

        // アプリ起動時に通知チャネルを作成する
        createNotificationChannel()

        // チェックと通知の実行
        checkExpiredItemsAndNotify()

        // 追加ボタンの押下時処理
        binding.addItemButton.setOnClickListener {
            val intent = Intent(this, SelectImageActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_SELECT_IMAGE)
        }

        // チェックボタンのクリックリスナー
        binding.checkButton.setOnClickListener {
            checkExpiredItemsAndNotify()
        }
    }

    // 結果表示
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_SELECT_IMAGE && resultCode == RESULT_OK) {
            data?.let {
                val imageResId = it.getIntExtra("selectedImage", 0)
                val name = it.getStringExtra("name") ?: "No name"
                val number = it.getIntExtra("number", 0)
                val expiryDate = it.getStringExtra("expiryDate") ?: ""

                val newItem = FridgeItem(imageResId, name, number, expiryDate)
                fridgeItems.add(newItem)
                fridgeItemAdapter.notifyItemInserted(fridgeItems.size - 1)
            }
        } else if (requestCode == REQUEST_CODE_EDIT_ITEM && resultCode == Activity.RESULT_OK) {
            val updatedItem = data?.getParcelableExtra<FridgeItem>("updatedItem")
            val deleteItem = data?.getParcelableExtra<FridgeItem>("deleteItem")

            // 更新時処理
            if (updatedItem != null) {
                updateItem(updatedItem)
            }
            // 削除時処理
            else if (deleteItem != null) {
                deleteItem(deleteItem)
            }
        }
    }

    // 修正した商品情報の反映
    private fun updateItem(updatedItem: FridgeItem?) {
        updatedItem?.let {
            val index = fridgeItems.indexOfFirst { it.imageResId == updatedItem.imageResId }
            if (index != -1) {
                fridgeItems[index] = updatedItem
                fridgeItemAdapter.notifyItemChanged(index)
            }
        }
    }

    // 削除した商品情報の反映deleteItem
    private fun deleteItem(deleteItem: FridgeItem?) {
        deleteItem?.let {
            val index = fridgeItems.indexOfFirst { it.imageResId == deleteItem.imageResId }
            if (index != -1) {
                fridgeItems.removeAt(index)
                fridgeItemAdapter.notifyItemRemoved(index)
            }
        }
    }

    companion object {
        const val REQUEST_CODE_SELECT_IMAGE = 1
        const val REQUEST_CODE_EDIT_ITEM = 2
    }

    //通知チャネルを作成
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "消費期限通知"
            val descriptionText = "消費期限が過ぎた商品の通知"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(notificationChannelId, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    // 消費期限チェック
    private fun checkExpiredItemsAndNotify() {
        val currentDate = Calendar.getInstance()

        for (item in fridgeItems) {
            val expiryDate = item.expiryDate

            try {
                val parsedExpiryDate = Calendar.getInstance()
                parsedExpiryDate.time = dateFormat.parse(expiryDate) ?: continue

                if (parsedExpiryDate.before(currentDate)) {
                    // 期限切れの商品がある場合、通知を表示する
                    showExpirationNotification(item)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // 消費期限チェック結果通知
    private fun showExpirationNotification(item: FridgeItem) {
        // 商品情報を含むIntentを設定
        val intent = Intent(this, EditItemActivity::class.java).apply {
            putExtra("item", item)
        }

        // アプリケーションの外部からEditItemActivity実行
        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // 通知を作成
        val builder = NotificationCompat.Builder(this, notificationChannelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("商品の消費期限が過ぎています")
            .setContentText("${item.expiryDate} に消費期限が過ぎている商品があります。")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent) // 通知をタップしたときのIntentを設定
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(this)) {
            notify(notificationId, builder.build())
        }
    }

    private fun editItem(item: FridgeItem) {
        val intent = Intent(this, EditItemActivity::class.java).apply {
            putExtra("item", item)
        }
        startActivityForResult(intent, REQUEST_CODE_EDIT_ITEM)
    }
}

