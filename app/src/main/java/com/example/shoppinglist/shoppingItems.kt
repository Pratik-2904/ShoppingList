package com.example.shoppinglist

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

data class ShoppingItems(
    val id: Int,
    val name: String,
    var quantity: Int,
    var isEditing: Boolean = false
)

//When usin gmore than one AlertDialog There is no need of following func
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingApp() {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(0.dp, 20.dp),
        color = MaterialTheme.colorScheme.background
    ) {
        var sItems by remember { mutableStateOf(listOf<ShoppingItems>()) }
        var statusAlertDialog by remember { mutableStateOf(false) }
        var itemName by remember { mutableStateOf("Item") }
        var itemQuan by remember { mutableStateOf("1") }

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {
//            Main button
            Button(
                onClick = {
                    statusAlertDialog = true
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)

            ) {
                Text(text = "New Item")

            }

//            Column for list of Items
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                items(sItems) {
                    ShoppingListItem(it,{},{})
                }
            }
        }
        if (statusAlertDialog) {
            AlertDialog(onDismissRequest = { statusAlertDialog = false },
                confirmButton = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Button(onClick = {
                            if (itemName.isNotBlank()) {
                                val newItem = ShoppingItems(
                                    id = sItems.size + 1,
                                    name = itemName,
                                    quantity = itemQuan.toInt()
                                )
                                sItems = sItems + newItem
                                statusAlertDialog = false
                                itemName = ""
                                itemQuan = ""
                            }
                        }) {
                            Text(text = "Add")
                        }

                        Button(onClick = {
                            statusAlertDialog = false
                        }) {
                            Text(text = "cancel")
                        }


                    }
                }

                ,title = { Text(text = "Add Item") }
                ,text = {
                    Column {
                            OutlinedTextField(
                                value = itemName,
                                onValueChange = { itemName = it },
                                singleLine = true,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp), label = { Text(text = "Item Name") }

                            )



                            OutlinedTextField(
                                value = itemQuan,
                                onValueChange = {
                                    itemQuan = it
                                }
                                ,singleLine = true
                                , modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                                , keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)

                            )



                    }
                }
            )
        }




    }
}
@Composable
fun ShoppingListItem(
    item:ShoppingItems,
    onEditClick:  () -> Unit,
    onDeleteClick: () -> Unit  //lamda functions

) {
    Row(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .border(
                border = BorderStroke(2.dp, Color(0xff018786)),
                shape = RoundedCornerShape(20)
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically

    ) {

        Text(text = item.name, modifier = Modifier.padding(8.dp))

        Text(text = item.quantity.toString(), modifier = Modifier.padding(8.dp))

        Row(
            modifier = Modifier.padding(8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {

            IconButton(onClick =  onEditClick) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit")
            }

            IconButton(onClick = onDeleteClick) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "Edit")
            }

        }
    }
}


