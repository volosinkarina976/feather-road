package com.BzNpXqL.vTmJrK.presentation.game

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.BzNpXqL.vTmJrK.R
import com.BzNpXqL.vTmJrK.core.config.GameConfig
import com.BzNpXqL.vTmJrK.core.theme.AccentCyan
import com.BzNpXqL.vTmJrK.core.theme.AccentGold
import com.BzNpXqL.vTmJrK.core.theme.AccentGreen
import com.BzNpXqL.vTmJrK.core.theme.AccentTerracotta
import com.BzNpXqL.vTmJrK.core.theme.CellDark
import com.BzNpXqL.vTmJrK.core.theme.CellLight
import com.BzNpXqL.vTmJrK.core.theme.LaneIdle
import com.BzNpXqL.vTmJrK.core.theme.SignPlate
import com.BzNpXqL.vTmJrK.core.ui.WoodPanel
import com.BzNpXqL.vTmJrK.domain.model.Cell
import com.BzNpXqL.vTmJrK.domain.model.RouteColor
import com.BzNpXqL.vTmJrK.domain.model.SignShape
import kotlin.math.floor
import kotlin.math.sin

fun routeTint(route: RouteColor): Color = when (route) {
    RouteColor.Amber -> AccentGold
    RouteColor.Cyan -> AccentCyan
    RouteColor.Moss -> AccentGreen
    RouteColor.Clay -> AccentTerracotta
}

@Composable
fun BoardView(
    state: GameUiState,
    onCellTap: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val board = state.board
    if (board.cols <= 0 || board.rows <= 0) return

    val shakeDrive by animateFloatAsState(
        targetValue = if (state.phase == Phase.Failed) 1f else 0f,
        animationSpec = tween(durationMillis = 420)
    )

    BoxWithConstraints(modifier = modifier, contentAlignment = Alignment.Center) {
        val frame = GameConfig.BOARD_PADDING_DP.dp + GameConfig.BOARD_BORDER_DP.dp
        val widthLimit = minOf(
            maxWidth - 32.dp,
            maxHeight - 16.dp,
            GameConfig.BOARD_MAX_W_DP.dp
        )
        val span = maxOf(board.cols, board.rows)
        val cell = floor(((widthLimit - frame * 2) / span).value)
            .coerceAtLeast(18f)
            .dp
        val boardWidth = cell * board.cols + frame * 2
        val boardHeight = cell * board.rows + frame * 2

        WoodPanel(
            modifier = Modifier
                .size(boardWidth, boardHeight)
                .graphicsLayer {
                    translationX = sin(shakeDrive * 25f) * 6f * density
                },
            corner = 18.dp,
            borderWidth = GameConfig.BOARD_BORDER_DP.dp,
            inset = GameConfig.BOARD_PADDING_DP.dp
        ) {
            Column {
                for (row in 0 until board.rows) {
                    Row {
                        for (col in 0 until board.cols) {
                            val index = row * board.cols + col
                            key(board.cols, index) {
                            BoardCell(
                                index = index,
                                cell = board.cellAt(index),
                                turns = state.turns.getOrElse(index) { 0 },
                                side = cell,
                                checker = (row + col) % 2 == 0,
                                owner = state.ownership[index],
                                completed = state.completedCells.contains(index),
                                crossed = state.crossedCells.contains(index),
                                flashing = state.flashingCells.contains(index),
                                enabled = state.phase == Phase.Idle || state.phase == Phase.Routing,
                                onCellTap = onCellTap
                            )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun BoardCell(
    index: Int,
    cell: Cell,
    turns: Int,
    side: Dp,
    checker: Boolean,
    owner: RouteColor?,
    completed: Boolean,
    crossed: Boolean,
    flashing: Boolean,
    enabled: Boolean,
    onCellTap: (Int) -> Unit
) {
    val base = if (checker) CellLight else CellDark
    Box(
        modifier = Modifier
            .size(side)
            .background(base),
        contentAlignment = Alignment.Center
    ) {
        when (cell) {
            is Cell.Grass -> GrassCell()
            is Cell.Source -> PortCell(
                tint = routeTint(cell.route),
                side = side,
                sprite = R.drawable.sprite_grain_sack,
                description = stringResource(R.string.cd_grain_sack),
                highlighted = completed
            )

            is Cell.Target -> PortCell(
                tint = routeTint(cell.route),
                side = side,
                sprite = R.drawable.sprite_feeder,
                description = stringResource(R.string.cd_feeder),
                highlighted = completed
            )

            is Cell.Sign -> SignCell(
                index = index,
                shape = cell.shape,
                turns = turns,
                side = side,
                owner = owner,
                crossed = crossed,
                flashing = flashing,
                enabled = enabled,
                onCellTap = onCellTap
            )
        }
    }
}

@Composable
private fun GrassCell() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val blade = size.minDimension * 0.22f
        for (step in 0 until 3) {
            val x = size.width * (0.28f + 0.22f * step)
            drawLine(
                color = AccentGreen.copy(alpha = 0.08f),
                start = Offset(x, size.height * 0.68f),
                end = Offset(x + blade * 0.3f, size.height * 0.68f - blade),
                strokeWidth = 3f,
                cap = StrokeCap.Round
            )
        }
    }
}

@Composable
private fun PortCell(
    tint: Color,
    side: Dp,
    sprite: Int,
    description: String,
    highlighted: Boolean
) {
    Box(
        modifier = Modifier.size(side),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(side - 8.dp)
                .clip(CircleShape)
                .background(tint.copy(alpha = if (highlighted) 0.34f else 0.20f))
                .border(
                    2.dp,
                    tint.copy(alpha = if (highlighted) 0.95f else 0.65f),
                    CircleShape
                )
        )
        if (highlighted) {
            Box(
                modifier = Modifier
                    .size(side)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            listOf(AccentGreen.copy(alpha = 0.22f), Color.Transparent)
                        )
                    )
            )
        }
        Image(
            painter = painterResource(sprite),
            contentDescription = description,
            contentScale = ContentScale.Fit,
            modifier = Modifier.size(side - 14.dp)
        )
    }
}

@Composable
private fun SignCell(
    index: Int,
    shape: SignShape,
    turns: Int,
    side: Dp,
    owner: RouteColor?,
    crossed: Boolean,
    flashing: Boolean,
    enabled: Boolean,
    onCellTap: (Int) -> Unit
) {
    val angle by animateFloatAsState(
        targetValue = turns * 90f,
        animationSpec = spring(
            dampingRatio = 0.55f,
            stiffness = Spring.StiffnessMediumLow
        )
    )
    val lane = when {
        crossed || flashing -> AccentTerracotta
        owner != null -> routeTint(owner)
        else -> LaneIdle
    }
    val laneWeight by animateFloatAsState(
        targetValue = if (owner != null) 1f else 0.72f,
        animationSpec = tween(260)
    )
    val signDescription = stringResource(R.string.cd_sign)
    val signState = if (owner != null) {
        stringResource(R.string.sd_connected)
    } else {
        stringResource(R.string.sd_open)
    }

    Box(
        modifier = Modifier
            .size(side)
            .clickable(enabled = enabled) { onCellTap(index) }
            .semantics {
                contentDescription = signDescription
                stateDescription = signState
                role = Role.Button
            },
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(side - 6.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(SignPlate)
                .border(
                    1.dp,
                    AccentGold.copy(alpha = if (crossed) 0.55f else 0.25f),
                    RoundedCornerShape(8.dp)
                )
        )
        Box(
            modifier = Modifier
                .size(side - 6.dp)
                .graphicsLayer { rotationZ = angle }
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val stroke = size.minDimension * 0.22f
                val centerX = size.width / 2f
                val centerY = size.height / 2f
                val path = Path()
                when (shape) {
                    SignShape.Straight -> {
                        path.moveTo(centerX, 0f)
                        path.lineTo(centerX, size.height)
                    }

                    SignShape.Elbow -> {
                        path.moveTo(centerX, 0f)
                        path.lineTo(centerX, centerY)
                        path.lineTo(size.width, centerY)
                    }
                }
                drawPath(
                    path = path,
                    color = lane.copy(alpha = laneWeight),
                    style = Stroke(
                        width = stroke,
                        cap = StrokeCap.Round,
                        join = StrokeJoin.Round
                    )
                )
            }
            Image(
                painter = painterResource(R.drawable.sprite_signpost),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .align(Alignment.Center)
                    .size((side - 6.dp) * 0.56f)
                    .alpha(0.55f)
            )
        }
    }
}
