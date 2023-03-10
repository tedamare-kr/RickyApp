package com.kroger.rickyapp

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import com.kroger.rickyapp.db.CharacterDao
import com.kroger.rickyapp.db.CharacterDatabase
import com.kroger.rickyapp.models.Character
import junit.framework.TestCase
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class CharacterDaoTest : TestCase() {

    private lateinit var characterDao: CharacterDao
    private lateinit var db: CharacterDatabase

    // Called everytime before executing the tests
    @Before
    fun createDB() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        // Creates a database on the RAM instead of persistence storage
        // DB will be cleared once the process is killed
        // Allow it to run on a single thread
        db = Room.inMemoryDatabaseBuilder(
            context,
            CharacterDatabase::class.java
        ).allowMainThreadQueries().build()
        characterDao = db.characterDao()
    }

    // Called everytime after executing the tests
    @After
    fun closeDB() {
        db.close()
    }

    @Test
    fun createAndReadCharacter(): Unit = runBlocking {
        val character = Character(
            id = 1,
            name = "Ricky Sanchez",
            status = "Alive",
            species = "Human",
            type = "na",
            gender = "Male",
            image = "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
            url = "https://rickandmortyapi.com/api/location/3",
            created = "2017-11-04T18:48:46.250Z"
        )

        characterDao.insertCharacter(character)

        val response = characterDao.getAllCharacterByID(1)
        assertThat(response).isEqualTo(character)
    }

    @Test
    fun deleteCharacter(): Unit = runBlocking {
        val character = Character(
            id = 1,
            name = "Ricky Sanchez",
            status = "Alive",
            species = "Human",
            type = "na",
            gender = "Male",
            image = "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
            url = "https://rickandmortyapi.com/api/location/3",
            created = "2017-11-04T18:48:46.250Z"
        )

        characterDao.insertCharacter(character)

        val response = characterDao.deleteCharacter(character)
        assertThat(response).isEqualTo(character)
    }
}
