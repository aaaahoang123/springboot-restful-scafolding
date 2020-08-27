package vn.amit.entity.auth

import org.springframework.security.core.GrantedAuthority
import javax.persistence.*
import org.springframework.security.core.userdetails.UserDetails
import vn.amit.common.entity.WithTimestampEntity
import vn.amit.token.TokenSubject

@Entity
@Table(name = "users")
open class User(
        @Column(unique = true, nullable = false)
        private var username: String? = null,

        @Column(nullable = false)
        private var password: String? = null,

        @Column(unique = true, nullable = false)
        open var email: String? = null,

        @Column(nullable = false, columnDefinition = "tinyint default ${UserStatus.ACTIVE}")
        open var status: Int = UserStatus.ACTIVE
) : WithTimestampEntity(), UserDetails, TokenSubject {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        open var id: Int? = null

        override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
               return mutableListOf()
        }

        override fun getPassword(): String? {
                return password
        }

        open fun setPassword(password: String?) {
                this.password = password
        }

        override fun getUsername(): String? {
                return username
        }

        open fun setUsername(username: String?) {
                this.username = username
        }

        override fun isAccountNonExpired(): Boolean {
               return true
        }

        override fun isAccountNonLocked(): Boolean {
                return true
        }

        override fun isCredentialsNonExpired(): Boolean {
                return true
        }

        override fun isEnabled(): Boolean {
                return status == UserStatus.ACTIVE
        }

        override fun getTokenIdentifier(): String {
                return username!!
        }

        override fun getCustomClaims(): Map<String, Any> {
                return mapOf()
        }
}