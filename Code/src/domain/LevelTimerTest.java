package domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * Verifica el comportamiento del temporizador del nivel.
 */
class LevelTimerTest {

    private static final int TIME_LIMIT = 30; // segundos para los tests

    private Level buildTimedLevel() {
        return Level.builder(1)
                .time(TIME_LIMIT)
                .build();
    }

    // ── Tests ─────────────────────────────────────────────────────────────

    @Test
    void levelInitializesWithFullTime() {
        Level level = buildTimedLevel();
        assertEquals(TIME_LIMIT, level.getTimeRemaining(), 0.001f,
                "El nivel debe iniciar con el tiempo completo");
    }

    @Test
    void getLevelTimeReturnsConfiguredLimit() {
        Level level = buildTimedLevel();
        assertEquals(TIME_LIMIT, level.getLevelTime(),
                "getLevelTime debe devolver el límite configurado");
    }

    @Test
    void timeDecrementsAfterTickTime() {
        Level level = buildTimedLevel();
        level.tickTime(5.0f);
        assertEquals(TIME_LIMIT - 5.0f, level.getTimeRemaining(), 0.001f,
                "El tiempo restante debe decrementarse en 5 segundos");
    }

    @Test
    void isTimeUpReturnsFalseBeforeExpiry() {
        Level level = buildTimedLevel();
        level.tickTime(TIME_LIMIT - 1.0f); // queda 1 segundo
        assertFalse(level.isTimeUp(),
                "isTimeUp debe ser false cuando aún queda tiempo");
    }

    @Test
    void isTimeUpReturnsTrueWhenExactlyZero() {
        Level level = buildTimedLevel();
        level.tickTime(TIME_LIMIT); // consume todo el tiempo
        assertTrue(level.isTimeUp(),
                "isTimeUp debe ser true cuando el tiempo llega exactamente a 0");
    }

    @Test
    void isTimeUpReturnsTrueAfterOverflow() {
        Level level = buildTimedLevel();
        level.tickTime(TIME_LIMIT * 10f); // mucho más del límite
        assertTrue(level.isTimeUp(),
                "isTimeUp debe ser true cuando se supera el tiempo límite");
    }

    @Test
    void timeRemainingDoesNotGoBelowZero() {
        Level level = buildTimedLevel();
        level.tickTime(TIME_LIMIT * 100f);
        assertEquals(0f, level.getTimeRemaining(), 0.001f,
                "El tiempo restante no debe ser negativo");
    }

    @Test
    void resetTimeRestoresFullTimer() {
        Level level = buildTimedLevel();
        level.tickTime(20.0f); // consume parte del tiempo
        level.resetTime();
        assertEquals(TIME_LIMIT, level.getTimeRemaining(), 0.001f,
                "resetTime debe restaurar el tiempo al límite original");
        assertFalse(level.isTimeUp(),
                "isTimeUp debe ser false tras resetTime");
    }

    @Test
    void multipleTicksAccumulate() {
        Level level = buildTimedLevel();
        level.tickTime(5.0f);
        level.tickTime(5.0f);
        level.tickTime(5.0f);
        assertEquals(TIME_LIMIT - 15.0f, level.getTimeRemaining(), 0.001f,
                "Múltiples tickTime deben acumularse correctamente");
    }
}
