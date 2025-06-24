package com.example.simbirsoft_android_practice

import com.example.simbirsoft_android_practice.domain.usecase.ProfileUseCase
import com.example.simbirsoft_android_practice.model.Friend
import javax.inject.Inject

class ProfileUseCaseImpl @Inject constructor() : ProfileUseCase {

    override fun loadFriends(): List<Friend> {
        return listOf(
            Friend(
                1,
                "Алексис Санчес",
                "https://photobooth.cdn.sports.ru/preset/tc_person/4/02/2c8b043f747e8b03764db15fc1d2d.png"
            ),
            Friend(
                2,
                "Деклан Райс",
                "https://photobooth.cdn.sports.ru/preset/tags/3/1a/c964ab3eb44d883cca720b243570a.png"
            ),
            Friend(
                3,
                "Букайо Сака",
                "https://photobooth.cdn.sports.ru/preset/tc_person/a/8b/0e7d6eba2431fa68d0275d1124d82.jpeg"
            ),
            Friend(
                4,
                "Алексей Гладков",
                "https://thumb.tildacdn.com/tild3739-3337-4530-b562-643539663265/-/format/webp/_.jpg"
            ),
            Friend(
                5,
                "Кирилл Розов",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQNtxwOwoQCubf4BzQpq4erjTloyf3O2uUblg&s"
            ),
            Friend(
                6,
                "Райан Гослинг",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS_Gn1Em872bptcqUX2Yytct8--VEYCUv3kwQ&s"
            )
        )
    }
}