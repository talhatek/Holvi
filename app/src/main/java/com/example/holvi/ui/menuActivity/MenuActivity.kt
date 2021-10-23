package com.example.holvi.ui.menuActivity

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Scaffold
import com.example.holvi.R
import com.example.holvi.theme.HolviTheme
import com.example.holvi.ui.addActivity.AddActivity
import com.example.holvi.ui.allActivity.AllActivity
import com.example.holvi.ui.common.composable.TopAppBarOnlyIcon
import com.example.holvi.ui.deleteActivity.DeleteActivity
import com.example.holvi.utils.MenuType
import com.example.holvi.utils.MenuType.Companion.ADD
import com.example.holvi.utils.MenuType.Companion.DELETE
import com.example.holvi.utils.MenuType.Companion.GENERATE
import com.example.holvi.utils.MenuType.Companion.SEE_ALL
import com.example.holvi.utils.MenuType.Companion.UPDATE
import kotlin.system.exitProcess

class MenuActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HolviTheme {
                Scaffold(topBar = {
                    TopAppBarOnlyIcon(res = R.drawable.ic_power) {
                        this.finish()
                    }
                },
                    content = {
                        MenuMainScreen(
                            itemList = mutableListOf<@MenuType String>(
                                ADD,
                                UPDATE,
                                SEE_ALL,
                                DELETE,
                                GENERATE
                            )
                        ) {
                            when (it) {
                                ADD -> {
                                    startActivity(
                                        Intent(
                                            this@MenuActivity,
                                            AddActivity::class.java
                                        )
                                    )
                                }
                                UPDATE -> {
                                }
                                SEE_ALL -> {
                                    startActivity(
                                        Intent(
                                            this@MenuActivity,
                                            AllActivity::class.java
                                        )
                                    )
                                }
                                DELETE -> {
                                    startActivity(
                                        Intent(
                                            this@MenuActivity,
                                            DeleteActivity::class.java
                                        )
                                    )
                                }
                                GENERATE -> {
                                }

                            }
                        }
                    }

                )
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        exitProcess(0)
    }
}

