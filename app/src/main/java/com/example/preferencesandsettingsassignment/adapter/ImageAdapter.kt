package com.example.preferencesandsettingsassignment.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import com.example.preferencesandsettingsassignment.R


class ImageAdapter(private var context: Context, private val listOfAllImages: ArrayList<String>) : BaseAdapter() {

    override fun getCount(): Int = listOfAllImages.size


    override fun getItem(postion: Int): Any = postion


    override fun getItemId(postion: Int): Long = 0


    override fun getView(postion: Int, view: View?, parent: ViewGroup?): View? {
        var vi: View? = view
        if (vi== null) {
            val inflater=LayoutInflater.from(context)
            vi= inflater.inflate(R.layout.item_gallery,parent, false)
            val image: ImageView = vi.findViewById<View>(R.id.imgFolderImage) as ImageView
            image.setImageURI(android.net.Uri.parse(listOfAllImages[postion]))
            listOfAllImages.removeAt(postion)
        }
        return vi
    }
}
