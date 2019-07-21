package com.arnab.notepad.views.activity

import android.os.AsyncTask
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.arnab.notepad.R
import com.arnab.notepad.databinding.ActivityAddNoteBinding
import com.arnab.notepad.db.NoteDatabase
import com.arnab.notepad.db.NoteRepository
import com.arnab.notepad.models.Note
import com.arnab.notepad.views.viewhelpers.ObservableEditText
import kotlinx.android.synthetic.main.activity_add_note.*
import java.lang.ref.WeakReference

class AddNoteActivity : AppCompatActivity() {


    lateinit var repository: NoteRepository
    var noteId: Long? = null
    lateinit var binding: ActivityAddNoteBinding
    val titleEditText = ObservableEditText()
    val contentEditText = ObservableEditText()

    companion object {
        //This is compile one time only. So taking currentTime like this is a wrong way.
        public val lastSeen: Long = System.currentTimeMillis()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_note)

        //Binding can be done this way for multiple variable
        binding.apply {
            title = titleEditText
            content = contentEditText
        }
        //Or we can do Binding normally in this way for single item
        //binding.title = titleEditText
        //binding.content = contentEditText

        val bundle = intent.extras
        if (bundle != null) {

            noteId = bundle.getLong("NoteId")
            val titleString: String = bundle.getString("title")!!
            val contentString: String = bundle.getString("content")!!

            titleEditText.text = titleString
            contentEditText.text = contentString
        }

        val noteDao = NoteDatabase.getDatabase(this).noteDao()
        repository = NoteRepository(noteDao)


        /////////////////////////////////////////////////////////////////
        // Thread and Runnable code For checking LiveData working or not
        /////////////////////////////////////////////////////////////////
        val runnable = Runnable {
            for (count in 1..5) {
                Thread.sleep(5000)
                InsertNoteDbAsync(this).execute(
                    Note(
                        0,
                        titleEditText.text,
                        contentEditText.text,
                        System.currentTimeMillis()
                    )
                )
            }
        }
        val thread = Thread(runnable)
        //thread.start()

        //////////////////////////////////////////////
        // Used lambda expression for OnClickListener
        //////////////////////////////////////////////
        floatingActionButton.setOnClickListener {
            if (noteId === null)
            //////////////////////////////////////////
            //check if the EditText have values or not
            //////////////////////////////////////////
                if (titleEditText.text.isNotEmpty()) {
                    InsertNoteDbAsync(this).execute(
                        Note(
                            0,
                            titleEditText.text,
                            contentEditText.text,
                            System.currentTimeMillis()
                        )
                    )
                    titleEditText.text = ""
                    contentEditText.text = ""
                } else {
                    Toast.makeText(this, "Please add subject", Toast.LENGTH_SHORT).show()
                }
            else {
                UpdateNoteDbAsyncTask().execute(
                    Note(
                        noteId!!,
                        titleEditText.text,
                        contentEditText.text,
                        System.currentTimeMillis()
                    )
                )
            }
        }


        //DbAsync1().execute()

    }

    //////////////////////////////////////
    // Memory leaks may occur is this way.
    //////////////////////////////////////
    inner class DbAsync1 : AsyncTask<Void, Void, Void>() {
        override fun doInBackground(vararg params: Void?): Void? {
            repository.insert(Note(0, "", "", System.currentTimeMillis()))
            return null
        }
    }

    //////////////////////////////////////////
    // This way we can avoid the Memory leaks
    //////////////////////////////////////////
    private class InsertNoteDbAsync internal constructor(context: AddNoteActivity) : AsyncTask<Note, Void, Void>() {
        private val activityReference: WeakReference<AddNoteActivity> = WeakReference(context)
        override fun doInBackground(vararg params: Note): Void? {
            activityReference.get()?.repository?.insert(params[0])
            return null

        }

        override fun onPostExecute(result: Void?) {
            super.onPostExecute(result)
            activityReference.get()?.finish()
        }
    }


    inner class UpdateNoteDbAsyncTask : AsyncTask<Note, Void, Int>() {

        override fun doInBackground(vararg params: Note): Int {
            return repository.updateNote(params[0])
        }

        override fun onPostExecute(result: Int?) {
            super.onPostExecute(result)
            if (result == 1)
                Toast.makeText(this@AddNoteActivity, "Saved", Toast.LENGTH_SHORT).show()
            this@AddNoteActivity.finish()
        }

    }
}
