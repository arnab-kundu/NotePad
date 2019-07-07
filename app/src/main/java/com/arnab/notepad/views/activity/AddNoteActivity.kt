package com.arnab.notepad.views.activity

import android.os.AsyncTask
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.arnab.notepad.R
import com.arnab.notepad.db.NoteDatabase
import com.arnab.notepad.db.NoteRepository
import com.arnab.notepad.models.Note
import kotlinx.android.synthetic.main.activity_add_note.*
import java.lang.ref.WeakReference

class AddNoteActivity : AppCompatActivity() {


    lateinit var repository: NoteRepository
    var noteId: Long? = null

    companion object {
        //This is compile one time only. So taking currentTime like this is a wrong way.
        public val lastSeen: Long = System.currentTimeMillis()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note)

        val bundle = intent.extras
        noteId = bundle?.getLong("NoteId")
        val title: String? = bundle?.getString("title")
        val content: String? = bundle?.getString("content")

        et_subject.setText(title)
        et_description.setText(content)


        val noteDao = NoteDatabase.getDatabase(this).noteDao()
        repository = NoteRepository(noteDao)


        ////////////////////////////
        // Thread and Runnable code
        ////////////////////////////
        val runnable = Runnable {
            for (count in 1..100) {
                Thread.sleep(5000)
                DbAsync(this).execute()
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
                if (et_subject.text.toString().isNotEmpty()) {
                    DbAsync(this).execute(
                        Note(
                            0,
                            et_subject.text.toString(),
                            et_description.text.toString(),
                            System.currentTimeMillis()
                        )
                    )
                    et_subject.text.clear()
                    et_description.text.clear()
                } else {
                    Toast.makeText(this, "Please add subject", Toast.LENGTH_SHORT).show()
                }
            else {
                UpdateNoteDbAsyncTask().execute(
                    Note(
                        noteId!!,
                        et_subject.text.toString(),
                        et_description.text.toString(),
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
    private class DbAsync internal constructor(context: AddNoteActivity) : AsyncTask<Note, Void, Void>() {
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
