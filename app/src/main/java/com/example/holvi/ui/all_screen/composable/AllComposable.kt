package com.example.holvi.ui.all_screen.composable

import android.content.ClipData
import android.content.ClipboardManager
import androidx.activity.ComponentActivity
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.animation.core.animateSizeAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.awaitHorizontalTouchSlopOrCancellation
import androidx.compose.foundation.gestures.drag
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import com.example.holvi.R
import com.example.holvi.db.model.Password
import com.example.holvi.theme.*
import com.example.holvi.ui.add_screen.AddPasswordState
import com.example.holvi.ui.add_screen.AddViewModel
import com.example.holvi.ui.add_screen.composable.InputView
import com.example.holvi.ui.add_screen.composable.PasswordInputView
import com.example.holvi.ui.all_screen.AllViewModel
import com.example.holvi.ui.all_screen.DeletePasswordState
import com.example.holvi.ui.all_screen.PasswordsState
import com.example.holvi.ui.common.composable.TopAppBarBackWithLogo
import com.example.holvi.utils.SnackbarController
import kotlinx.coroutines.launch
import org.koin.androidx.compose.get
import kotlin.math.abs

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllScreen(navController: NavController) {

    val primaryColor = MaterialTheme.colorScheme.primary
    val allViewModel = get<AllViewModel>()
    val passwordsState = allViewModel.allPasswords.collectAsState().value

    val scope = rememberCoroutineScope()
    val snackbarController = SnackbarController(scope = scope)
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val state = rememberLazyListState()

    var showAddBottomSheet by remember { mutableStateOf(false) }
    var showUpdateBottomSheet by remember { mutableStateOf<UpdateAction>(UpdateAction.Null) }

    var fabPosition by remember {
        mutableStateOf(Offset.Zero)
    }
    var fabColor by remember {
        mutableStateOf(primaryColor)
    }
    val fabColorState = animateColorAsState(targetValue = fabColor, label = "fabColor")

    val modalSheet = rememberModalBottomSheetState(
        confirmValueChange = { true },
        skipPartiallyExpanded = true,
    )
    LaunchedEffect(key1 = true, block = {
        allViewModel.passwordDeleteState.collect {
            when (it) {
                is DeletePasswordState.Success -> {
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar(
                            message = "Successfully deleted!",
                            actionLabel = "Undo",
                            duration = SnackbarDuration.Short
                        ).also { result ->
                            if (result == SnackbarResult.ActionPerformed) {
                                allViewModel.undoDelete()
                            }
                        }
                    }
                }

                is DeletePasswordState.Undo -> snackbarHostState.showSnackbar(message = "Password recovered!")

                else -> snackbarHostState.showSnackbar("Something went wrong!")
            }
        }
    })

    Scaffold(
        topBar = {
            TopAppBarBackWithLogo {
                navController.popBackStack()
            }
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier
                    .testTag("addFab")
                    .onGloballyPositioned {
                        fabPosition = it.positionInWindow()
                    },
                onClick = { showAddBottomSheet = true },
                shape = CircleShape,
                containerColor = fabColorState.value
            ) {
                Icon(
                    imageVector = Icons.Filled.Create,
                    contentDescription = "add",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) { paddingValues ->
        when (passwordsState) {
            is PasswordsState.Loading -> {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator()
                }
            }

            is PasswordsState.Success -> {
                Column(
                    modifier = Modifier.padding(top = paddingValues.calculateTopPadding())
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 16.dp, top = 16.dp, bottom = 16.dp),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        Search(viewModel = allViewModel)
                    }

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                            .padding(end = 8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(
                            16.dp,
                            Alignment.Top
                        ),
                        contentPadding = PaddingValues(top = 16.dp),
                        state = state,
                        content = {
                            items(
                                passwordsState.data,
                                key = { password -> password.id }
                            ) { item ->
                                PasswordItem(
                                    modifier = Modifier,
                                    fabPosition = fabPosition,
                                    threshold = .3f,
                                    password = item,
                                    onCopied = {
                                        coroutineScope.launch {
                                            snackbarHostState.showSnackbar(
                                                "Password copied to clipboard!"
                                            )
                                        }
                                    },
                                    onDelete = {
                                        allViewModel.delete(item.siteName)
                                    },
                                    onUpdate = {
                                        showUpdateBottomSheet = UpdateAction.Loaded(item)
                                    },
                                    onUpdateFabColor = { color ->
                                        if (state.layoutInfo.visibleItemsInfo.any { visibleItem -> visibleItem.key == item.id }) {
                                            fabColor = color
                                        }
                                    })
                            }
                        },
                    )

                }


                if (showUpdateBottomSheet is UpdateAction.Loaded) {
                    UpdateModalSheet(
                        sheetState = modalSheet,
                        onDismiss = { isItemUpdated ->
                            if (isItemUpdated) {
                                snackbarController.showSnackbar(
                                    snackbarHostState,
                                    "Password updated successfully!"
                                )
                                showUpdateBottomSheet = UpdateAction.Null
                            }
                        },
                        item = (showUpdateBottomSheet as UpdateAction.Loaded).password
                    )
                }
            }

            is PasswordsState.Empty -> {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize(),
                ) {
                    Text(
                        text = "You don't have any saved password.",
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                }
            }

            is PasswordsState.Error -> {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(
                        text = passwordsState.message,
                        textAlign = TextAlign.Center
                    )
                }
            }

            else -> Unit
        }

        if (showAddBottomSheet) {
            AddModalSheet(
                sheetState = modalSheet,
                onDismiss = { isItemAdded ->
                    showAddBottomSheet = false
                    if (isItemAdded) {
                        snackbarController.showSnackbar(
                            snackbarHostState,
                            "Password inserted successfully!"
                        )
                    }
                },
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddModalSheet(
    sheetState: SheetState,
    onDismiss: (isNewItemAdded: Boolean) -> Unit,
) {
    val myAddViewModel = get<AddViewModel>()
    val passwordState =
        myAddViewModel.passwordAddState.collectAsState(initial = AddPasswordState.Empty).value

    val snackState = remember { SnackbarHostState() }
    val snackScope = rememberCoroutineScope()

    var siteName by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var userName by remember { mutableStateOf("") }

    LaunchedEffect(passwordState) {
        when (passwordState) {
            is AddPasswordState.Success -> {
                onDismiss.invoke(true)
            }

            is AddPasswordState.Failure -> {
                snackScope.launch {
                    snackState.showSnackbar(
                        passwordState.message
                    )
                }
            }

            else -> Unit
        }
    }
    ModalBottomSheet(
        onDismissRequest = { onDismiss.invoke(false) },
        sheetState = sheetState,
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .testTag("addSheet")
            .imePadding(),
        containerColor = MaterialTheme.colorScheme.background
    ) {
        Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                InputView(
                    hintParam = "Site Name",
                    viewModel = myAddViewModel,
                    onValueChanged = { siteName = it })
                InputView(
                    hintParam = "User Name",
                    viewModel = myAddViewModel,
                    onValueChanged = { userName = it })
                PasswordInputView(
                    hintParam = "Password",
                    viewModel = myAddViewModel,
                    onValueChanged = { password = it })

                Button(modifier = Modifier
                    .padding(bottom = 16.dp)
                    .testTag("addButton"), onClick = {
                    myAddViewModel.addPassword(
                        Password(
                            id = 0,
                            siteName = siteName,
                            password = password,
                            userName = userName
                        )
                    )
                }) {
                    Text(text = "Add")
                }


                SnackbarHost(hostState = snackState, Modifier)
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateModalSheet(
    sheetState: SheetState,
    onDismiss: (isItemUpdated: Boolean) -> Unit,
    item: Password
) {
    val myAddViewModel = get<AddViewModel>()
    val passwordState =
        myAddViewModel.passwordAddState.collectAsState(initial = AddPasswordState.Empty).value

    val snackState = remember { SnackbarHostState() }
    val snackScope = rememberCoroutineScope()

    var siteName by remember { mutableStateOf(item.siteName) }
    var password by remember { mutableStateOf(item.password) }
    var userName by remember { mutableStateOf(item.userName) }

    LaunchedEffect(passwordState) {
        when (passwordState) {
            is AddPasswordState.Success -> {
                onDismiss.invoke(true)
            }

            is AddPasswordState.Failure -> {
                snackScope.launch {
                    snackState.showSnackbar(
                        passwordState.message
                    )
                }
            }

            else -> Unit
        }
    }
    ModalBottomSheet(
        onDismissRequest = { onDismiss.invoke(false) },
        sheetState = sheetState,
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .imePadding(),
        containerColor = MaterialTheme.colorScheme.background
    ) {
        Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                InputView(
                    hintParam = "Site Name",
                    defaultValue = siteName,
                    viewModel = myAddViewModel,
                    onValueChanged = { siteName = it })
                InputView(
                    hintParam = "User Name",
                    defaultValue = userName,
                    viewModel = myAddViewModel,
                    onValueChanged = { userName = it })
                PasswordInputView(
                    hintParam = "Password",
                    viewModel = myAddViewModel,
                    defaultValue = password,
                    onValueChanged = { password = it })

                Button(modifier = Modifier.padding(bottom = 16.dp), onClick = {
                    myAddViewModel.addPassword(
                        Password(
                            id = item.id,
                            siteName = siteName,
                            password = password,
                            userName = userName
                        )
                    )
                }) {
                    Text(text = "Update")
                }

                SnackbarHost(hostState = snackState, Modifier)
            }
        }
    }
}

@Composable
fun Search(viewModel: AllViewModel) {
    var isExpanded by remember {
        mutableStateOf(false)
    }
    val textFocusRequester = remember {
        FocusRequester()
    }

    var searchQuery by remember {
        mutableStateOf("")
    }
    LaunchedEffect(key1 = searchQuery) {
        viewModel.searchQuery.emit(searchQuery)
    }

    val screenSizeDp = DpSize(
        LocalConfiguration.current.screenWidthDp.dp,
        LocalConfiguration.current.screenHeightDp.dp
    )
    val searchSize by animateSizeAsState(
        targetValue = if (isExpanded)
            Size(screenSizeDp.width.value, 60f)
        else
            Size(40f, 40f),
        animationSpec = tween(200, easing = LinearEasing),
        label = "searchBackgroundSizeAnimation", finishedListener = {
            if (isExpanded) {
                textFocusRequester.requestFocus()
            }
        }
    )
    Box(
        modifier = Modifier
            .size(searchSize.width.dp, searchSize.height.dp)
            .clip(CircleShape)
            .background(color = MaterialTheme.colorScheme.primary),
        contentAlignment = Alignment.Center
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isExpanded)
                TextField(
                    modifier = Modifier
                        .focusRequester(textFocusRequester),
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        cursorColor = Color.White,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    singleLine = true,
                )
            Crossfade(
                targetState = isExpanded,
                label = "searchAnimation"
            ) { state ->

                Icon(
                    modifier = Modifier
                        .clickable {
                            if (state) {
                                viewModel.getAll()
                            }
                            searchQuery = ""
                            isExpanded = isExpanded.not()

                        },
                    painter = painterResource(id = if (state) R.drawable.ic_close else R.drawable.ic_search_24),
                    contentDescription = "search",
                    tint = Color.White
                )
            }
        }
    }
}

@Composable
fun PasswordItem(
    modifier: Modifier,
    fabPosition: Offset,
    threshold: Float,
    password: Password,
    onCopied: () -> Unit,
    onDelete: () -> Unit,
    onUpdate: () -> Unit,
    onUpdateFabColor: (color: Color) -> Unit,
) {
    require(threshold in (0f..1f))
    var cardOffset by rememberSaveable { mutableIntStateOf(0) }
    var invokeDeleteOnAnimationFinish by remember { mutableStateOf(false) }
    var anchorThreshold by remember { mutableIntStateOf(0) }
    val openDialog = remember { mutableStateOf(false) }
    val cardSize = remember { mutableStateOf(IntSize.Zero) }
    var passwordText by remember { mutableStateOf("*".repeat(password.password.length)) }
    var resId by remember { mutableIntStateOf(R.drawable.ic_invisible) }
    var visible by remember { mutableStateOf(false) }

    val animatedCardOffset =
        animateIntOffsetAsState(
            targetValue = IntOffset(cardOffset, 0),
            finishedListener = {
                if (invokeDeleteOnAnimationFinish and (it.x == 0)) {
                    onDelete.invoke()
                    invokeDeleteOnAnimationFinish = false
                }
            },
            label = "cardOffsetAnimation"
        )

    val density = LocalDensity.current
    val context = LocalContext.current

    Box(
        modifier = modifier
            .fillMaxWidth(.85f)
            .onSizeChanged {
                anchorThreshold = it.width
                    .times(threshold)
                    .toInt()
            }
            .onGloballyPositioned {
                if (fabPosition.y in it.positionInWindow().y..it.positionInWindow().y.plus(cardSize.value.height)
                ) {

                    onUpdateFabColor.invoke(if (password.id % 2 != 0) SecondPrimaryDark else PrimaryGreen)
                }
            }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .height(with(density) { cardSize.value.height.toDp() })
                .clip(CardDefaults.shape)
                .drawBehind {
                    drawRect(
                        Color(0xFF6c757d),
                        size = Size(this.size.width.div(2), this.size.height)
                    )
                    drawRect(
                        Color(0xffB31B1B),
                        topLeft = Offset(this.size.width.div(2f), 0f),
                        size = Size(this.size.width.div(2), this.size.height)
                    )
                }
                .clickable {
                    if (cardOffset < 0) {
                        openDialog.value = true
                    } else {
                        onUpdate.invoke()
                        cardOffset = 0
                    }
                }
                .padding(
                    horizontal = with(density) {
                        (anchorThreshold
                            .toDp()
                            .minus(24.dp)
                            .div(2)).coerceAtLeast(0.dp)
                    }
                ),
            contentAlignment = Alignment.CenterEnd
        ) {
            Row {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "update",
                    tint = Color.White,
                    modifier = Modifier
                        .size(24.dp)
                )
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "delete",
                    tint = Color.White,
                    modifier = Modifier
                        .size(24.dp)
                )

            }
        }
        Card(
            modifier = Modifier
                .fillMaxSize()
                .onSizeChanged {
                    cardSize.value = it
                }
                .offset { animatedCardOffset.value }
                .pointerInput(Unit) {
                    detectHorizontalDragGesturesTransparently(
                        onDragEnd = {
                            val action = listOf(
                                DragAction.Delete(-anchorThreshold),
                                DragAction.Still(),
                                DragAction.Update(anchorThreshold)
                            ).minBy { abs(it.value - cardOffset) }
                            cardOffset = when (action) {
                                is DragAction.Still -> 0
                                is DragAction.Delete -> -anchorThreshold
                                is DragAction.Update -> anchorThreshold
                            }
                        },
                        onDrag = { _, dragAmount ->
                            cardOffset =
                                (cardOffset + dragAmount.toInt()).coerceIn(
                                    -anchorThreshold,
                                    anchorThreshold
                                )
                        })
                },
            elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
            colors = CardDefaults.cardColors(containerColor = if (password.id % 2 == 0) SecondPrimaryDark else PrimaryGreen)
        ) {
            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    text = password.siteName,
                    style = TextStyle(
                        fontFamily = PoppinsSemiBold,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                    ),
                    color = Color.White,
                    maxLines = 1,
                )
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            modifier = Modifier.fillMaxWidth(.45f),
                            text = passwordText,
                            style = TextStyle(
                                fontFamily = PoppinsRegular,
                                fontWeight = FontWeight.Normal,
                                fontSize = 12.sp,
                                textAlign = TextAlign.Center,
                            ),
                            color = Color.White,
                            maxLines = 1
                        )
                        Spacer(Modifier.fillMaxWidth(.05f))
                        CompositionLocalProvider(LocalRippleTheme provides RippleCustomTheme(isOdd = (password.id % 2 != 0))) {
                            IconButton(
                                onClick = {
                                    if (visible) {
                                        passwordText = "*".repeat(password.password.length)
                                        resId = R.drawable.ic_invisible
                                    } else {
                                        passwordText = password.password
                                        resId = R.drawable.ic_visible
                                    }
                                    visible = !visible

                                },
                                enabled = true,
                                modifier = Modifier
                                    .size(36.dp)
                            ) {
                                Icon(
                                    painter = painterResource(id = resId),
                                    contentDescription = "hiddenOrShown",
                                    tint = Color.White,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                        Spacer(Modifier.fillMaxWidth(.05f))
                        CompositionLocalProvider(LocalRippleTheme provides RippleCustomTheme(isOdd = (password.id % 2 != 0))) {
                            IconButton(
                                onClick = {
                                    try {
                                        val clipboardManager =
                                            context.getSystemService(ComponentActivity.CLIPBOARD_SERVICE) as ClipboardManager
                                        val clipData =
                                            ClipData.newPlainText(
                                                "label",
                                                password.password
                                            )
                                        clipboardManager.setPrimaryClip(clipData)
                                        onCopied.invoke()
                                    } catch (_: Exception) {
                                    }
                                },
                                enabled = true,
                                modifier = Modifier
                                    .size(36.dp)


                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_copy),
                                    contentDescription = "hiddenOrShown",
                                    tint = Color.White,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    }
                }
                Text(
                    text = password.userName,
                    style = TextStyle(
                        fontFamily = PoppinsRegular,
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                    ),
                    color = Color.White,
                    maxLines = 1,
                )

            }
        }
    }
    if (openDialog.value) {
        ConfirmDeleteAlertDialog(
            siteName = password.siteName,
            onDismiss = {
                openDialog.value = false
                cardOffset = 0
            }
        ) {
            openDialog.value = false
            invokeDeleteOnAnimationFinish = true
            cardOffset = 0
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmDeleteAlertDialog(siteName: String, onDismiss: () -> Unit, onConfirm: () -> Unit) {
    AlertDialog(
        onDismissRequest = {
            onDismiss.invoke()
        }
    ) {
        Surface(
            modifier = Modifier
                .wrapContentSize()
                .wrapContentHeight(),
            tonalElevation = AlertDialogDefaults.TonalElevation,
            shape = Shapes.extraLarge,
            color = MaterialTheme.colorScheme.background,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            ) {

                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                color = Color.White,
                                fontWeight = FontWeight.Light
                            ),
                        ) {
                            append("Are you sure about deleting")
                        }
                        append(" ")
                        withStyle(
                            style = SpanStyle(
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        ) {
                            append(siteName)
                        }
                        withStyle(style = SpanStyle(color = Color.White)) {
                            append("?")
                        }
                    },
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(24.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,

                    ) {

                    TextButton(
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xffB31B1B).copy(
                                .9f
                            )
                        ),
                        shape = Shapes.medium,
                        onClick = {
                            onConfirm.invoke()
                        },
                    ) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = "Yes",
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    TextButton(
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary.copy(
                                alpha = .2f
                            )
                        ),
                        shape = Shapes.medium,
                        onClick = {
                            onDismiss.invoke()
                        },
                    ) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = "Cancel",
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

suspend fun PointerInputScope.detectHorizontalDragGesturesTransparently(
    onDragStart: (Offset) -> Unit = { },
    onDragEnd: () -> Unit = { },
    onDragCancel: () -> Unit = { },
    onDrag: (change: PointerInputChange, dragAmount: Float) -> Unit
) {
    awaitEachGesture {
        val down = awaitFirstDown(requireUnconsumed = false)
        var drag: PointerInputChange?
        var overSlop = 0f
        do {
            drag = awaitHorizontalTouchSlopOrCancellation(
                down.id,
            ) { change, over ->
                change.consume()
                overSlop = over
            }
        } while (drag != null && !drag.isConsumed)
        if (drag != null) {
            onDragStart.invoke(drag.position)
            onDrag(drag, overSlop)
            if (
                !drag(drag.id) {
                    onDrag(it, it.positionChange().x)
                    it.consume()
                }
            ) {
                onDragCancel()
            } else {
                onDragEnd()
            }
        }
    }
}

sealed class DragAction {
    abstract val value: Int

    data class Delete(override val value: Int) : DragAction()
    data class Update(override val value: Int) : DragAction()
    data class Still(override val value: Int = 0) : DragAction()
}

sealed class UpdateAction {

    data object Null : UpdateAction()
    data class Loaded(val password: Password) : UpdateAction()
}

private class RippleCustomTheme(private val isOdd: Boolean) : RippleTheme {
    @Composable
    override fun defaultColor() =
        RippleTheme.defaultRippleColor(
            if (isOdd) SecondPrimaryDark else PrimaryGreen,
            lightTheme = true
        )

    @Composable
    override fun rippleAlpha(): RippleAlpha = RippleAlpha(.6f, .6f, .6f, .6f)

}