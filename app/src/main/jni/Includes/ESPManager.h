struct enemy_t {
    void *object;
};

class ESPManager {
public:
    std::vector<enemy_t *> *enemies;
    ESPManager() {
        enemies = new std::vector<enemy_t *>();
    }

    bool isEnemyPresent(void *enemyObject) const {
        for (auto & enemy : *enemies) {
            if (enemy->object == enemyObject) {
                return true;
            }
        }
        return false;
    }

    void tryAddEnemy(void *enemyObject) const {
        if (isEnemyPresent(enemyObject)) {
            return;
        }
        enemy_t *newEnemy = new enemy_t();
        newEnemy->object = enemyObject;
        enemies->push_back(newEnemy);
    }

    void removeEnemyGivenObject(void *enemyObject) const {
        for (int i = 0; i < enemies->size(); i++) {
            if ((*enemies)[i]->object == enemyObject) {
                enemies->erase(enemies->begin() + i);
                return;
            }
        }
    }
};

ESPManager *espManager;