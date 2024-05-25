package com.example.shoppinglist

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
//import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
//import androidx.compose.material3.TopAppBar
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
    var name: String,
    var quantity: Int,
    var isEditing: Boolean = false
)

//When using more than one AlertDialog There is no need of following func
//@OptIn(ExperimentalMaterial3Api::class)
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
            Spacer(modifier = Modifier.padding(12.dp))
//            Main button

            Button(
                onClick = { statusAlertDialog = true },
                modifier = Modifier.align(Alignment.CenterHorizontally)

            ) {
                Text(text = "New Item")
//                //an unnamed function which executes and stores the data into the variable
//                // the double no is var of type int and lambda function is of INt type Separated by the '->' which shows the implementation of lambda function for the variable
//                val doubleNumber: (Int) -> Int = {
//                    it*2
//                }

//                Text(doubleNumber.toString())
            }

//            Column for list of Items
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                items(sItems) {
//                    ShoppingListItem(it, {}, {})//demo
                    item ->   //can give name to it like this or can use it directly
                    if(item.isEditing){
                        ShoppingItemsEditor(item = item, onEditComplete = {
                            editedName,editedQuantity ->
                            sItems = sItems.map{it.copy(isEditing = false)}
                            val editedItem = sItems.find { it.id == item.id } //function used to find specific
                            editedItem?.let {
                                it.name = editedName
                                it.quantity = editedQuantity
                                
                            }
                        })
                    }else{
                        ShoppingListItem(item = item, onEditClick = {
                            //finding out which item we are editing and changing
                            sItems = sItems.map { it.copy(isEditing = it.id == item.id )}
                        },
                            onDeleteClick = {
                                sItems = sItems - item
                            })

                    }
                    
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
                }, title = { Text(text = "Add Item") }, text = {
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
                            },
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)

                        )


                    }
                }
            )
        }


    }
}

@Composable
fun ShoppingListItem(
    item: ShoppingItems,
    //lamda expression without input and output
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit  //lambda functions

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

        Text(text = "Qty:${item.quantity}", modifier = Modifier.padding(8.dp))

        Row(
            modifier = Modifier.padding(8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {

            IconButton(onClick = onEditClick) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit")
            }

            IconButton(onClick = onDeleteClick) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "Edit")
            }

        }
    }
}

@Composable

fun ShoppingItemsEditor(item: ShoppingItems, onEditComplete: (String, Int) -> Unit) {
    var editedName by remember {
        mutableStateOf(item.name)
    }

    var editedQuantity by remember {
        mutableStateOf(item.quantity.toString())
    }
    var isEditing by remember {
        mutableStateOf(item.isEditing)
    }

    Row (
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(8.dp)
            ,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Column {
            BasicTextField(value = editedName, onValueChange = {
                editedName = it
            },
                singleLine = true,
                modifier = Modifier.wrapContentSize(align = Alignment.Center)
            )

            BasicTextField(value = editedQuantity, onValueChange = {
                editedQuantity = it
            },
                singleLine = true,
                modifier = Modifier.wrapContentSize(align = Alignment.Center)
            )
        }
        Button(onClick = {
            isEditing = false
            onEditComplete(editedName,editedQuantity.toIntOrNull() ?: 1)

        }) {
            Text(text = "Save")
        }
    }


}

//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun TopBAR(title: String){
//    TopAppBar(title = {
//        Text(text = "Shopping Cart App")
//
//
//    },
//        )
//}


