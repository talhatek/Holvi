package com.tek.password.ui

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.animation.core.animateSizeAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.awaitHorizontalTouchSlopOrCancellation
import androidx.compose.foundation.gestures.drag
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
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
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.coerceAtLeast
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import com.tek.database.model.Password
import com.tek.password.R
import com.tek.password.presentation.AddPasswordState
import com.tek.password.presentation.AddViewModel
import com.tek.password.presentation.AllViewModel
import com.tek.password.presentation.DeletePasswordState
import com.tek.password.presentation.PasswordsState
import com.tek.ui.HolviScaffold
import com.tek.ui.HolviTheme
import com.tek.ui.Screen
import com.tek.ui.SnackbarController
import com.tek.ui.TopAppBarBackWithLogo
import com.tek.ui.holviButtonColors
import kotlinx.coroutines.launch
import org.koin.androidx.compose.get
import kotlin.math.abs

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllScreen(navController: NavController) {

    val primaryColor = HolviTheme.colors.primaryBackground
    val allViewModel = get<AllViewModel>()
    val passwordsState = allViewModel.allPasswords.collectAsState().value
    val passwordDeleteState = allViewModel.passwordDeleteState.collectAsState(initial = null).value

    val scope = rememberCoroutineScope()
    val snackbarController = SnackbarController(scope = scope)
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val screenHeight = LocalContext.current.resources.displayMetrics.heightPixels

    val state = rememberLazyListState()

    var showAddBottomSheet by remember { mutableStateOf(false) }
    var showUpdateBottomSheet by remember { mutableStateOf<UpdateAction>(UpdateAction.Null) }

    var fabPosition by remember {
        mutableStateOf(Offset.Zero)
    }
    var fabColor by remember {
        mutableStateOf(primaryColor)
    }
    var fabYOffset by remember {
        mutableStateOf(0.dp)
    }
    val fabColorState = animateColorAsState(targetValue = fabColor, label = "fabColor")
    val fabYOffsetState = animateDpAsState(targetValue = fabYOffset, label = "fabYOffset")

    val modalSheet = rememberModalBottomSheetState(
        confirmValueChange = { true },
        skipPartiallyExpanded = true,
    )
    val isScrollingUp = state.isScrollingUp()

    LaunchedEffect(passwordDeleteState) {
        passwordDeleteState?.let {
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
    }

    LaunchedEffect(isScrollingUp) {
        fabYOffset = if (isScrollingUp) 0.dp else (screenHeight - fabPosition.y).dp
    }

    HolviScaffold(
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
                    .offset(x = 0.dp, y = fabYOffsetState.value)
                    .onGloballyPositioned {
                        if (fabPosition == Offset.Zero) {
                            fabPosition = it.positionInWindow()
                        }
                    },
                onClick = { navController.navigate(Screen.AddScreen.route) },
                shape = CircleShape,
                containerColor = fabColorState.value
            ) {
                Icon(
                    imageVector = Icons.Filled.Create,
                    contentDescription = "add",
                    tint = HolviTheme.colors.primaryForeground,
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

            }

            is PasswordsState.Empty -> {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize(),
                ) {
                    Text(
                        text = "You don't have any saved password.",
                        color = HolviTheme.colors.appForeground,
                        style = HolviTheme.typography.body
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
                        style = HolviTheme.typography.body
                    )
                }
            }

            else -> Unit
        }

        if (showAddBottomSheet) {
            AddModalSheet(
                modifier = Modifier
                    .wrapContentHeight()
                    .fillMaxWidth()
                    .imePadding()
                    .testTag("addSheet"),
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

        if (showUpdateBottomSheet is UpdateAction.Loaded) {
            UpdateModalSheet(
                sheetState = modalSheet,
                onDismiss = { isItemUpdated ->
                    if (isItemUpdated) {
                        snackbarController.showSnackbar(
                            snackbarHostState,
                            "Password updated successfully!"
                        )
                    }
                    showUpdateBottomSheet = UpdateAction.Null

                },
                item = (showUpdateBottomSheet as UpdateAction.Loaded).password
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddModalSheet(
    modifier: Modifier,
    sheetState: SheetState,
    onDismiss: (isNewItemAdded: Boolean) -> Unit,
) {
    val phoneNavBar = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        (LocalContext.current as Activity).windowManager.currentWindowMetrics.windowInsets.getInsets(
            WindowInsetsCompat.Type.navigationBars()
        ).bottom
    } else {
        64
    }


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
        modifier = modifier,
        onDismissRequest = { onDismiss.invoke(false) },
        sheetState = sheetState,
        containerColor = HolviTheme.colors.appBackground
    ) {
        Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
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
                    .testTag("addButton"),
                    colors = holviButtonColors(),
                    onClick = {
                        myAddViewModel.addPassword(
                            Password(
                                id = 0,
                                siteName = siteName,
                                password = password,
                                userName = userName
                            )
                        )
                    }) {
                    Text(text = "Add", style = HolviTheme.typography.body)
                }
                Spacer(
                    modifier = Modifier
                        .height(with(LocalDensity.current) { phoneNavBar.toDp() })
                        .fillMaxWidth()
                        .background(Color.Gray)
                )
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
        containerColor = HolviTheme.colors.appBackground
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
                    onValueChanged = { userName = it },
                )
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
                }, colors = holviButtonColors()) {
                    Text(
                        text = "Update",
                        style = HolviTheme.typography.body
                    )
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
            .background(color = HolviTheme.colors.primaryBackground),
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
                        cursorColor = HolviTheme.colors.primaryForeground,
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
                    painter = painterResource(id = if (state) R.drawable.ic_close else R.drawable.ic_search),
                    contentDescription = "search",
                    tint = HolviTheme.colors.primaryForeground

                )
            }
        }
    }
}

@Composable
fun getItemColors(id: Int): Pair<Color, Color> {
    val background =
        if (id % 2 == 0) {
            HolviTheme.colors.primaryDarkBackground
        } else {
            HolviTheme.colors.primaryBackground
        }
    val foreground = if (id % 2 == 0) {
        HolviTheme.colors.appForeground
    } else {
        HolviTheme.colors.primaryForeground
    }
    return background to foreground
}

@Composable
fun getOppositeBackgroundColor(color: Color) =
    if (color == HolviTheme.colors.primaryDarkBackground) HolviTheme.colors.primaryBackground else HolviTheme.colors.primaryDarkBackground

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
    val itemColors = getItemColors(id = password.id)
    val oppositeColor = getOppositeBackgroundColor(itemColors.first)

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
                if (fabPosition.y in it.positionInWindow().y..it.positionInWindow().y.plus(
                        cardSize.value.height
                    )
                ) {
                    onUpdateFabColor.invoke(oppositeColor)
                }
            }
    ) {
        val deleteBackground = HolviTheme.colors.error
        Box(
            modifier = Modifier
                .fillMaxSize()
                .height(with(density) { cardSize.value.height.toDp() })
                .clip(CardDefaults.shape)
                .drawBehind {
                    drawRect(
                        oppositeColor,
                        size = Size(size.width.div(2), size.height)
                    )
                    drawRect(
                        deleteBackground,
                        topLeft = Offset(size.width.div(2f), 0f),
                        size = Size(size.width.div(2), size.height)
                    )
                }
                .clickable {
                    if (cardOffset < 0) {
                        openDialog.value = true
                    } else {
                        onUpdate.invoke()
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
                    tint = itemColors.first,
                    modifier = Modifier
                        .size(24.dp)
                )
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "delete",
                    tint = HolviTheme.colors.appForeground,
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
            colors = CardDefaults.cardColors(containerColor = itemColors.first)
        ) {
            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    text = password.siteName,
                    style = HolviTheme.typography.body,
                    color = itemColors.second,
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
                            style = HolviTheme.typography.body,
                            color = itemColors.second,
                            maxLines = 1
                        )
                        Spacer(Modifier.fillMaxWidth(.05f))
                        CompositionLocalProvider(
                            LocalRippleTheme provides RippleCustomTheme(
                                isOdd = (password.id % 2 != 0)
                            )
                        ) {
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
                                    tint = itemColors.second,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                        Spacer(Modifier.fillMaxWidth(.05f))
                        CompositionLocalProvider(
                            LocalRippleTheme provides RippleCustomTheme(
                                isOdd = (password.id % 2 != 0)
                            )
                        ) {
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
                                    tint = itemColors.second,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    }
                }
                Text(
                    text = password.userName,
                    style = HolviTheme.typography.body,
                    color = itemColors.second,
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
    BasicAlertDialog(onDismissRequest = {
        onDismiss.invoke()
    }
    ) {
        Surface(
            modifier = Modifier
                .wrapContentSize()
                .wrapContentHeight(),
            tonalElevation = AlertDialogDefaults.TonalElevation,
            shape = MaterialTheme.shapes.extraLarge,
            color = HolviTheme.colors.appBackground,
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
                                color = HolviTheme.colors.appForeground,
                                fontWeight = FontWeight.Light
                            ),
                        ) {
                            append("Are you sure about deleting")
                        }
                        append(" ")
                        withStyle(
                            style = SpanStyle(
                                color = HolviTheme.colors.appForeground,
                                fontWeight = FontWeight.Bold
                            )
                        ) {
                            append(siteName)
                        }
                        withStyle(
                            style = SpanStyle(
                                color = HolviTheme.colors.appForeground
                            )
                        ) {
                            append("?")
                        }
                    },
                    color = HolviTheme.colors.appForeground,
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
                            containerColor = HolviTheme.colors.error
                        ),
                        shape = MaterialTheme.shapes.medium,

                        onClick = {
                            onConfirm.invoke()
                        },
                    ) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = "Yes",
                            style = HolviTheme.typography.body
                        )
                    }

                    TextButton(
                        colors = ButtonDefaults.buttonColors(
                            containerColor = HolviTheme.colors.primaryBackground.copy(
                                alpha = .2f
                            )
                        ),
                        shape = MaterialTheme.shapes.medium,
                        onClick = {
                            onDismiss.invoke()
                        },
                    ) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = "Cancel",
                            style = HolviTheme.typography.body
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun LazyListState.isScrollingUp(): Boolean {
    var previousIndex by remember(this) { mutableIntStateOf(firstVisibleItemIndex) }
    var previousScrollOffset by remember(this) { mutableIntStateOf(firstVisibleItemScrollOffset) }
    return remember(this) {
        derivedStateOf {
            if (previousIndex != firstVisibleItemIndex) {
                previousIndex > firstVisibleItemIndex
            } else {
                previousScrollOffset >= firstVisibleItemScrollOffset
            }.also {
                previousIndex = firstVisibleItemIndex
                previousScrollOffset = firstVisibleItemScrollOffset
            }
        }
    }.value
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
            if (isOdd) HolviTheme.colors.primaryDarkBackground else HolviTheme.colors.primaryBackground,
            lightTheme = true
        )

    @Composable
    override fun rippleAlpha(): RippleAlpha = RippleAlpha(.6f, .6f, .6f, .6f)

}