package com.example.workmanager

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters


class MyWorker(context: Context, params: WorkerParameters) : Worker(context, params) {
    override fun doWork(): Result {
        // Background मा गर्ने काम
        Log.d("WorkManager", "Background task executed!")
        // काम सफल भयो भने
        return Result.success()
    }
}

// 2️⃣ MainActivity
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // UI: Button
            Button(onClick = { startWork() }) {
                Text("Start Background Task")
            }
        }
    }

    private fun startWork() {
        // Constraints define
        val constraints = Constraints.Builder()
            .setRequiresCharging(false)
            .build()

        // Work Request
        val workRequest = OneTimeWorkRequestBuilder<MyWorker>()
            .setConstraints(constraints)
            .build()

        // WorkManager enqueue
        WorkManager.getInstance(this).enqueue(workRequest)

        // Task observe
        WorkManager.getInstance(this)
            .getWorkInfoByIdLiveData(workRequest.id)
            .observe(this) { workInfo ->
                if (workInfo != null && workInfo.state.isFinished) {
                    Toast.makeText(this, "Task Finished!", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
