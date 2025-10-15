package jme.jobpotunity.kumejobpotunity.repository;

import jme.jobpotunity.kumejobpotunity.domain.user.User; // <-- package အသစ်သို့ ပြောင်းလဲထားသည်
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // 💡 NOTE: User entity တွင် email field သာရှိသောကြောင့် findByUsername အစား findByEmail ဟု ပြင်ဆင်ထားသည်
    Optional<User> findByEmail(String email);
}

