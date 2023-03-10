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
    fun testUpsertCharacter(): Unit = runBlocking {
        val character = Character(id = 1, name = "Rick Sanchez", status = "Alive", species = "Human", type = "Genius", gender = "Male", image = "https://rickandmortyapi.com/api/character/avatar/1.jpeg", url = "https://rickandmortyapi.com/api/character/1", created = "2017-11-04T18:48:46.250Z")

        characterDao.upsertCharacter(character)

        val updatedCharacter = Character(
            id = 1,
            name = "Teddy Amare",
            status = "Alive and Well",
            species = "Human",
            type = "",
            gender = "Male",
            image = "none",
            url = "none",
            created = "July 18, 2001"
        )

        characterDao.upsertCharacter(updatedCharacter)

        val response = characterDao.getAllCharacters()
        assertThat(response).contains(updatedCharacter)
        assertThat(response).doesNotContain(character)
        assertThat(response).hasSize(1)
    }

    @Test
    fun testInsertAllCharacters(): Unit = runBlocking {
        val characters = listOf(
            Character(id = 1, name = "Rick Sanchez", status = "Alive", species = "Human", type = "Genius", gender = "Male", image = "https://rickandmortyapi.com/api/character/avatar/1.jpeg", url = "https://rickandmortyapi.com/api/character/1", created = "2017-11-04T18:48:46.250Z"),
            Character(id = 2, name = "Morty Smith", status = "Alive", species = "Human", type = "", gender = "Male", image = "https://rickandmortyapi.com/api/character/avatar/2.jpeg", url = "https://rickandmortyapi.com/api/character/2", created = "2017-11-04T18:50:21.651Z"),
            Character(id = 3, name = "Summer Smith", status = "Alive", species = "Human", type = "", gender = "Female", image = "https://rickandmortyapi.com/api/character/avatar/3.jpeg", url = "https://rickandmortyapi.com/api/character/3", created = "2017-11-04T19:09:56.428Z"),
            Character(id = 4, name = "Jerry Smith", status = "Alive", species = "Human", type = "", gender = "Male", image = "https://rickandmortyapi.com/api/character/avatar/4.jpeg", url = "https://rickandmortyapi.com/api/character/4", created = "2017-11-04T19:22:43.665Z"),
            Character(id = 5, name = "Beth Smith", status = "Alive", species = "Human", type = "", gender = "Female", image = "https://rickandmortyapi.com/api/character/avatar/5.jpeg", url = "https://rickandmortyapi.com/api/character/5", created = "2017-11-04T19:26:56.301Z"),
            Character(id = 7, name = "Birdperson", status = "Dead", species = "Alien", type = "Bird-Person", gender = "Male", image = "https://rickandmortyapi.com/api/character/avatar/12.jpeg", url = "https://rickandmortyapi.com/api/character/12", created = "2017-11-04T20:03:34.737Z")
        )

        characterDao.insertAllCharacters(characters)

        val result = characterDao.getAllCharacters()
        assertThat(result).isEqualTo(characters.sortedBy { it.name })
    }

    @Test
    fun testGetAllCharacters(): Unit = runBlocking {
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

        characterDao.upsertCharacter(character)

        val response = characterDao.getAllCharacters()
        assertThat(response).contains(character)
        assertThat(response).hasSize(1)
    }

    @Test
    fun testDeleteCharacter(): Unit = runBlocking {
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
        characterDao.upsertCharacter(character)
        characterDao.deleteCharacter(character)

        val responseList = characterDao.getAllCharacters()
        assertThat(responseList).doesNotContain(character)
    }
}
