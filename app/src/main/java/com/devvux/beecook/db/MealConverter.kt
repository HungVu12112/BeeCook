package com.devvux.beecook.db

import androidx.room.TypeConverter
import androidx.room.TypeConverters

@TypeConverters
class MealConverter { /////// file này để biên dịch , chuyển đổi kiểu dữ liệu Any về String và ngược lại
    @TypeConverter
    fun fromAnyToString(attributes:Any?):String{
        if (attributes == null){
            return ""
        }
        return attributes as String
    }
    @TypeConverter
    fun fromStringToAny(attributes: String?):Any{
        if (attributes == null)
            return ""
        return attributes as Any
    }
}