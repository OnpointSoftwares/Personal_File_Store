package com.example.personalfilesstore.adapters

    import android.content.Intent
    import android.view.LayoutInflater
    import android.view.View
    import android.view.ViewGroup
    import android.widget.ImageView
    import android.widget.TextView
    import android.widget.Toast
    import androidx.recyclerview.widget.RecyclerView
    import com.example.personalfilesstore.R
    import com.example.personalfilesstore.models.FileModel

class FilesAdapter(private val notifications: List<FileModel>) :
        RecyclerView.Adapter<FilesAdapter.FileAdapterViewHolder>() {

        class FileAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            fun bind(fileModel: FileModel) {
                itemView.findViewById<TextView>(R.id.file_name).text= fileModel.Name
                itemView.findViewById<TextView>(R.id.btnDownload).setOnClickListener {
                    Toast.makeText(itemView.context,"Want to download",Toast.LENGTH_LONG).show()
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileAdapterViewHolder {
            val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.file_item, parent, false)
            return FileAdapterViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: FileAdapterViewHolder, position: Int) {
            val notification = notifications[position]
            holder.bind(notification)
        }

        override fun getItemCount(): Int {
            return notifications.size
        }
    }