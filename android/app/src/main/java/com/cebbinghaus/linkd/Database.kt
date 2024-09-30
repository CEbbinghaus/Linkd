package com.cebbinghaus.linkd;

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Insert
import androidx.room.Room
import androidx.room.Delete
import androidx.room.Database
import androidx.room.RoomDatabase
import android.content.Context
import com.cebbinghaus.linkd.proto.Main

@Entity(tableName = "intent_record")
data class IntentRecord(
    @ColumnInfo(name = "action") val action: String,
    @ColumnInfo(name = "flags") val flags: Int,
    @ColumnInfo(name = "data") val data: String?,
) {
	@PrimaryKey(autoGenerate = true)
	var id: Int = 0;

	companion object {
		fun fromHistory(history: Main.History): IntentRecord {
			return IntentRecord(
				history.action,
				history.flags,
				history.data
			)
		}
	}

	fun toHistory(): Main.History {
		val builder = Main.History.newBuilder()
		
		builder.setAction(action)
		builder.setFlags(flags)
			
		data?.let {
			builder.setData(it)
		}

		return builder.build();
	}

}

@Dao
interface IntentHistoryDao {
    @Query("SELECT * FROM intent_record")
    fun getAll(): List<IntentRecord>

    @Insert
    fun insert(record: IntentRecord)

    @Insert
    fun insertAll(vararg records: IntentRecord)

    @Delete
    fun delete(record: IntentRecord)
}


@Database(entities = [IntentRecord::class], version = 1)
abstract class LinkDB : RoomDatabase() {
    abstract fun history(): IntentHistoryDao
}


class Database(applicationContext: Context) {
	val db = Room.databaseBuilder(
		applicationContext,
		LinkDB::class.java, "linkd.db"
	).allowMainThreadQueries().build();


	val history get() = db.history()
}