package es.gorillapp.misteri

import android.content.Context
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import java.io.*

class DBManager(context: Context): SQLiteOpenHelper(
    context, "Slides.db", null, 1) {

    private val myContext: Context = context
    private val DB_EVENT = "Misteri"
    private val DB_PATH = myContext.resources.getString(R.string.db_path)
    private val DB_NAME = "Slides.db"
    private var myDataBase: SQLiteDatabase? = null


    override fun onCreate(db: SQLiteDatabase) {
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        TODO()
    }

    override fun onOpen(db: SQLiteDatabase?) {
        super.onOpen(db)
    }

    @Throws(SQLException::class)
    fun open() {
        // Open the data base
        try {
            createDataBase()
        } catch (e: IOException) {
            throw java.lang.Error("Error creating database")
        }
        val myPath: String = DB_PATH + DB_NAME
        myDataBase =
            SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.NO_LOCALIZED_COLLATORS)
    }

    /**
     * Create a new db schema in the device external storage and import the data
     */
    @Throws(IOException::class)
    fun createDataBase() {
        val dbExist: Boolean = checkDataBase()
        if (dbExist) {
            // If db exists don�t do nothing
        } else {
            // Create the database structure and then replace with ours
            this.readableDatabase
            try {
                this.close()
                copyDataBase()
            } catch (e: IOException) {
                throw Error("Error copying database$e")
            }
        }
    }

    /**
     * Check if the db exists to avoid copying verytime
     * @return true if exists, false if not
     */
    private fun checkDataBase(): Boolean {
        var dbExists = false
        try {
            val myPath: String = DB_PATH + DB_NAME
            val dbfile = File(myPath)
            dbExists = dbfile.exists()
        } catch (e: SQLiteException) {
            println("Database doesn't exist")
        }
        return dbExists
    }

    /**
     * Copy the assets db in the new db schema created in the device external storage
     */
    @Throws(IOException::class)
    private fun copyDataBase() {
        // Open the input database stream (assets)
        val myInput: InputStream = myContext.assets.open(DB_NAME)

        // New db structure�s path
        val outFileName: String = DB_PATH + DB_NAME

        // Open the output database strem (data/../databases)
        val myOutput: OutputStream = FileOutputStream(outFileName)

        // Copy bytes from input db to output db
        val buffer = ByteArray(1024)
        var length: Int
        while (myInput.read(buffer).also { length = it } > 0) {
            myOutput.write(buffer, 0, length)
        }

        // Free the streams
        myOutput.flush()
        myOutput.close()
        myInput.close()
    }


    companion object {
        // If you change the database schema, you must increment the database version.
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "Slides.db"

    }
}