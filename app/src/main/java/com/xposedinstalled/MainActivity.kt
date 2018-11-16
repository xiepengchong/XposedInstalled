package com.xposedinstalled

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class MainActivity : AppCompatActivity(), View.OnClickListener {
    override fun onClick(v: View?) {

        if (v?.id == install.id) {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.setDataAndType(Uri.parse("file://" + Environment.getExternalStorageDirectory().getAbsolutePath() + "/xposed.apk"),
                    "application/vnd.android.package-archive")
            this.startActivity(intent)
        } else if (v?.id == install_fw.id) {
            val pkg = "de.robv.android.xposed.installer"
            val clz = "de.robv.android.xposed.installer.WelcomeActivity"
            val i = Intent()
            i.setClassName(pkg, clz)
            this.startActivity(i)
        } else if (v?.id == reboot.id) {
            reboot();
        } else if (v?.id == enter.id) {
            val pkg = "de.robv.android.xposed.installer"
            val clz = "de.robv.android.xposed.installer.WelcomeActivity"
            val i = Intent()
            i.setClassName(pkg, clz)
            i.putExtra("fragment", 1);
            this.startActivity(i)
        }
    }

    private fun reboot() {
        try {
            val proc = Runtime.getRuntime().exec(arrayOf("su", "-c", "reboot "))  //关机
            proc.waitFor()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        copyXposed();
        install.setOnClickListener(this)
        enter.setOnClickListener(this)
        reboot.setOnClickListener(this)
        install_fw.setOnClickListener(this)
    }

    fun copyXposed() {
        val path: String = Environment.getExternalStorageDirectory().absolutePath + "/xposed.apk"
        val file: File = File(path)
        if (!file.exists()) {
            object : Thread() {
                public override fun run() {
                    copyApkFromAssets(this@MainActivity, "XposedInstaller_3.1.4.apk", path)
                }
            }.start()
        }
    }

    fun copyApkFromAssets(context: Context, fileName: String, path: String): Boolean {
        var copyIsFinish = false
        try {
            val inputString = context.getAssets().open(fileName)
            val file = File(path)
            file.createNewFile()
            val fos = FileOutputStream(file)
            val temp = ByteArray(1024)
            var i = 0

            while (inputString.read(temp).let { i = it;i > 0 }) {
                Log.v("XPC", "i" + i)
                fos.write(temp, 0, i)
            }
            fos.close()
            inputString.close()
            copyIsFinish = true
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return copyIsFinish
    }
}
