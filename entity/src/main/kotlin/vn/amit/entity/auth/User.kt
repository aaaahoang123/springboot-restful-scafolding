package vn.amit.entity.auth

import org.springframework.security.core.GrantedAuthority
import javax.persistence.*
import org.springframework.security.core.userdetails.UserDetails

@Entity
@Table(name = "users")
open class User(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        open var id: Int? = null,
        @Column(unique = true)
        private var username: String? = null,
        @Column(unique = true)
        private var email: String? = null
) : UserDetails {
        override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
               return mutableListOf()
        }

        override fun getPassword(): String {
                TODO("Not yet implemented")
        }

        override fun getUsername(): String {
                TODO("Not yet implemented")
        }

        fun setUsername(username: String?) {
                this.username = username
        }

        override fun isAccountNonExpired(): Boolean {
                TODO("Not yet implemented")
        }

        override fun isAccountNonLocked(): Boolean {
                TODO("Not yet implemented")
        }

        override fun isCredentialsNonExpired(): Boolean {
                TODO("Not yet implemented")
        }

        override fun isEnabled(): Boolean {
                TODO("Not yet implemented")
        }
}