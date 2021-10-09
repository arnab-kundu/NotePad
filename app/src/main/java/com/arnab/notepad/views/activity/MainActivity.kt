package com.arnab.notepad.views.activity

import android.content.Intent
import android.graphics.Color
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.arnab.notepad.R
import com.arnab.notepad.asynctask.DatabaseTask
import com.arnab.notepad.db.NoteDatabase
import com.arnab.notepad.db.NoteRepository
import com.arnab.notepad.models.Note
import com.arnab.notepad.models.dao.NoteDao
import com.arnab.notepad.views.SwipeToDeleteCallback
import com.arnab.notepad.views.adapter.NoteListAdapter
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), NoteListAdapter.NoteListItemClickListener {
    val notes = ArrayList<Note>()

    lateinit var repository: NoteRepository
    lateinit var mAdapter: NoteListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /////////////////////
        // Set DB connection
        /////////////////////
        val noteDao: NoteDao = NoteDatabase.getDatabase(this).noteDao()
        repository = NoteRepository(noteDao)

        //////////////////////////////////////////////////////////////////////////////
        // Set Adapter Only once then call notifyDataSetChange to update adapter data
        //////////////////////////////////////////////////////////////////////////////
        recycler_view.adapter = NoteListAdapter(notes, this)
        mAdapter = recycler_view.adapter as NoteListAdapter
        /*val viewModel: MainActivityViewModel = ViewModelProviders.of(this).get(MainActivityViewModel::class.java)
        viewModel.notes?.observe(this, Observer() {
            mAdapter.setData(it)
            Log.d("msg","coming")
        })*/
        //////////////////////////////////////////
        // Button Click Using Lambda expression
        //////////////////////////////////////////
        bt_add_note.setOnClickListener {
            val intent = Intent(this, AddNoteActivity::class.java)
            startActivity(intent)
        }

        //////////////////
        // AsyncTask Call
        //////////////////
        DBTask().execute()
    }

    ///////////////////////
    // DataBase AsyncTask
    ///////////////////////
    inner class DBTask : AsyncTask<Void, Void, List<Note>>() {


        override fun doInBackground(vararg params: Void?): List<Note> {
            return repository.getAllNotes()
        }

        override fun onPostExecute(result: List<Note>?) {
            super.onPostExecute(result)
            if (result != null) {
                notes.clear()
                for (note in result) {
                    notes.add(note)//Log.d("msg", note.toString())
                }
                recycler_view.adapter?.notifyDataSetChanged()
            }
            enableSwipeToDeleteAndUndo()
        }

    }

    override fun onResume() {
        super.onResume()
        //////////////////////////////////////////
        // AsyncTask Call to update data on Resume
        //////////////////////////////////////////
        DBTask().execute()
    }

    private fun enableSwipeToDeleteAndUndo() {

        //mAdapter = NoteListAdapter(notes)
        val swipeToDeleteCallback = object : SwipeToDeleteCallback(this@MainActivity) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, i: Int) {


                val position = viewHolder.adapterPosition
                val item = mAdapter.getData().get(position)

                val runnable = Runnable {
                    repository.deleteSelectedNote(notes[position].id)
                    DBTask().execute()
                    Thread.sleep(100)
                }
                val thread = Thread(runnable)
                thread.start()
                //mAdapter.removeItem(position)
                //mAdapter.notifyDataSetChanged()


                val snackbar = Snackbar.make(root, "Item was removed from the list.", Snackbar.LENGTH_LONG)
                snackbar.setAction("UNDO") {
                    val run = Runnable {
                        DatabaseTask().execute(item)
                        DBTask().execute()
                        Thread.sleep(100)
                    }
                    Thread(run).start()
                    mAdapter.notifyDataSetChanged()
                    //mAdapter.restoreItem(item, position)
                    //recycler_view.scrollToPosition(position)
                }

                snackbar.setActionTextColor(Color.YELLOW)
                snackbar.show()

            }
        }

        val itemTouchhelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchhelper.attachToRecyclerView(recycler_view)
    }

    override fun onItemClick(noteId: Long) {
        SelectFromDbTask().execute(noteId)
    }

    inner class SelectFromDbTask : AsyncTask<Long, Void, Note>() {
        override fun doInBackground(vararg params: Long?): Note {
            return repository.getSelectedNote(params[0])
        }

        override fun onPostExecute(result: Note?) {
            super.onPostExecute(result)
            val intent = Intent(this@MainActivity, AddNoteActivity::class.java)
            intent.putExtra("NoteId", result?.id)
            intent.putExtra("title", result?.title)
            intent.putExtra("content", result?.content)
            startActivity(intent)
        }

    }

}
/**
 * List is Immutable here. We cannot change List data. No ADD, REMOVE oppression.
 * ArrayList is Mutable. We can change the data. ADD, REMOVE oppression possible.
 */
