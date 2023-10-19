package com.example.testapplication

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import java.io.File

class MainActivity : AppCompatActivity() {

    private lateinit var downloadManager: DownloadManager
    private var downloadQueueId = 0L

    private val downloadCompleteReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val reference = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1) ?: -1

            if (downloadQueueId == reference) {
                val query = DownloadManager.Query()
                query.setFilterById(reference)

                val cursor = downloadManager.query(query)
                cursor.moveToFirst()

                val columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)
                val columnReason = cursor.getColumnIndex(DownloadManager.COLUMN_REASON)

                val status = cursor.getInt(columnIndex)
                val reason = cursor.getInt(columnReason)

                cursor.close()

                when (status) {
                    DownloadManager.STATUS_SUCCESSFUL -> {
                        Toast.makeText(context, "다운로드를 완료하였습니다.", Toast.LENGTH_SHORT).show()
                    }
                    DownloadManager.STATUS_PAUSED -> {
                        Toast.makeText(context, "다운로드가 중단되었습니다.", Toast.LENGTH_SHORT).show()
                    }
                    DownloadManager.STATUS_FAILED -> {
                        Toast.makeText(context, "다운로드를 실패하였습니다.", Toast.LENGTH_SHORT).show()
                    }
                }

                Log.d("download", "download status = $status :: reason = $reason")
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        pdfButtonClick()
        downloadFile()
    }

    private fun pdfButtonClick() {
        val button1 = findViewById<Button>(R.id.button1)
        val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).path + "/test.pdf")
        val uri = FileProvider.getUriForFile(this, applicationContext.packageName + ".provider", file)
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(uri, "application/pdf")
        intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION

        button1.setOnClickListener {
            startActivity(intent)
        }
    }

    private fun downloadFile() {
        val outputFilePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).path + "/test.pdf"
        downloadManager = baseContext.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

        val outputFile = File(outputFilePath)
        val button2 = findViewById<Button>(R.id.button2)
        val request = DownloadManager.Request(Uri.parse("http://9005.ipdisk.co.kr:9005/list/HDD1/드라이버/01.%20삼성/k4250/사용자%20메뉴얼.pdf"))
        request.setTitle("다운로드 항목")
        request.setDestinationUri(Uri.fromFile(outputFile))
        request.setAllowedOverMetered(true)

        button2.setOnClickListener {
            downloadQueueId = downloadManager.enqueue(request)
        }
    }

    override fun onResume() {
        super.onResume()
        val completeFilter = IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        registerReceiver(downloadCompleteReceiver, completeFilter)
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(downloadCompleteReceiver)
    }
}