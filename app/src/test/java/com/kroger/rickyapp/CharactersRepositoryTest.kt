package com.kroger.rickyapp

import com.google.common.truth.Truth.assertThat
import com.kroger.rickyapp.db.CharacterDao
import com.kroger.rickyapp.models.Character
import com.kroger.rickyapp.models.CharacterResponse
import com.kroger.rickyapp.network.RickAndMortyService
import com.kroger.rickyapp.ui.characters.CharacterUIData
import com.kroger.rickyapp.ui.characters.CharactersRepository
import com.kroger.rickyapp.util.TimeUtils
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkAll
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class CharactersRepositoryTest {
    // Don't use unless necessary
    private val mockApiService: RickAndMortyService = mockk()
    private val mockCharacterDao: CharacterDao = mockk()
    private val mockCharacterResponse: Response<CharacterResponse> = mockk()
    private val charactersRepository: CharactersRepository = CharactersRepository(mockApiService, mockCharacterDao)

    @Before
    fun beforeTests() {
        mockkObject(TimeUtils)
    }

    @Test
    fun test_fetch_data_with_empty_list() {
    }

    private fun define_data(): List<Character> {
        return listOf(
            Character(id = 1, name = "Rick Sanchez", status = "Alive", species = "Human", type = "Genius", gender = "Male", image = "https://rickandmortyapi.com/api/character/avatar/1.jpeg", url = "https://rickandmortyapi.com/api/character/1", created = "2017-11-04T18:48:46.250Z"),
            Character(id = 2, name = "Morty Smith", status = "Alive", species = "Human", type = "", gender = "Male", image = "https://rickandmortyapi.com/api/character/avatar/2.jpeg", url = "https://rickandmortyapi.com/api/character/2", created = "2017-11-04T18:50:21.651Z"),
            Character(id = 3, name = "Summer Smith", status = "Alive", species = "Human", type = "", gender = "Female", image = "https://rickandmortyapi.com/api/character/avatar/3.jpeg", url = "https://rickandmortyapi.com/api/character/3", created = "2017-11-04T19:09:56.428Z"),
            Character(id = 4, name = "Jerry Smith", status = "Alive", species = "Human", type = "", gender = "Male", image = "https://rickandmortyapi.com/api/character/avatar/4.jpeg", url = "https://rickandmortyapi.com/api/character/4", created = "2017-11-04T19:22:43.665Z"),
            Character(id = 5, name = "Beth Smith", status = "Alive", species = "Human", type = "", gender = "Female", image = "https://rickandmortyapi.com/api/character/avatar/5.jpeg", url = "https://rickandmortyapi.com/api/character/5", created = "2017-11-04T19:26:56.301Z"),
            Character(id = 7, name = "Birdperson", status = "Dead", species = "Alien", type = "Bird-Person", gender = "Male", image = "https://rickandmortyapi.com/api/character/avatar/12.jpeg", url = "https://rickandmortyapi.com/api/character/12", created = "2017-11-04T20:03:34.737Z")
        )
    }

    @Test
    fun given_getTheChars_when_elapsed_time_is_less_than_5_minutes_then_should_fetch_from_database(): Unit = runBlocking {
        // create the sut
        val expectedResults = define_data()
        every { TimeUtils.getCurrentTimeMillis() } returns 100_000L
        every { mockCharacterDao.getAllCharacters() } returns expectedResults

        // when
        val result = charactersRepository.getTheChars()

        // then
        verify { mockCharacterDao.getAllCharacters() }

        assertThat(result).isInstanceOf(CharacterUIData.Success::class.java)
        assertThat((result as CharacterUIData.Success).characterResponse.results).isEqualTo(expectedResults)
    }

    @Test
    fun given_getTheChars_when_elapsed_time_is_greater_than_5_minutes_then_should_fetch_list_from_api(): Unit = runBlocking {
        // create the sut
        val expectedResults = define_data()
        every { TimeUtils.getCurrentTimeMillis() } returns 500_000L
        coEvery { mockApiService.getAllCharacters() } returns mockCharacterResponse
        every { mockCharacterResponse.isSuccessful } returns true
        every { mockCharacterResponse.body() } returns CharacterResponse(define_data())

        coEvery { mockCharacterDao.insertAllCharacters(any()) } returns define_data().map { it.id.toLong() }
        // when
        val result = charactersRepository.getTheChars()

        // then
        coVerify { mockApiService.getAllCharacters() }
        coVerify { mockCharacterDao.insertAllCharacters(expectedResults) }

        assertThat(result).isInstanceOf(CharacterUIData.Success::class.java)
        assertThat((result as CharacterUIData.Success).characterResponse.results).isEqualTo(expectedResults)
    }

    @Test
    fun given_getTheChars_when_elapsed_time_is_greater_than_5_minutes_then_should_fetch_empty_list_from_api(): Unit = runBlocking {
        // create the sut
        val expectedResults = emptyList<Character>()
        every { TimeUtils.getCurrentTimeMillis() } returns 500_000L
        coEvery { mockApiService.getAllCharacters() } returns mockCharacterResponse
        every { mockCharacterResponse.isSuccessful } returns true
        every { mockCharacterResponse.body() } returns CharacterResponse(emptyList())

        coEvery { mockCharacterDao.insertAllCharacters(any()) } returns define_data().map { it.id.toLong() }
        // when
        val result = charactersRepository.getTheChars()

        // then
        coVerify { mockApiService.getAllCharacters() }
        coVerify { mockCharacterDao.insertAllCharacters(expectedResults) }

        assertThat(result).isInstanceOf(CharacterUIData.Success::class.java)
        assertThat((result as CharacterUIData.Success).characterResponse.results).isEqualTo(expectedResults)
    }

    @Test
    fun given_api_call_when_response_is_not_successful_then_should_return_error(): Unit = runBlocking {
        // create the sut
        every { TimeUtils.getCurrentTimeMillis() } returns 500_000L
        coEvery { mockApiService.getAllCharacters() } returns mockCharacterResponse
        every { mockCharacterResponse.isSuccessful } returns false

        // when
        val result = charactersRepository.getTheChars()

        // then
        coVerify { mockApiService.getAllCharacters(1) }

        assertThat(result).isInstanceOf(CharacterUIData.Error::class.java)
    }

    @After
    fun afterTests() {
        // static objcets / constructor
        unmockkAll()
    }
}
