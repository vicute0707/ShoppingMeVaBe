// Client-side validation using jQuery
$(document).ready(function () {
    // Generic form validation
    $('form').on('submit', function (e) {
        var form = this;
        var isValid = true;

        // Clear previous validation messages
        $(form).find('.is-invalid').removeClass('is-invalid');
        $(form).find('.invalid-feedback').hide();

        // Validate required fields
        $(form).find('[required]').each(function () {
            if (!this.checkValidity()) {
                $(this).addClass('is-invalid');
                $(this).siblings('.invalid-feedback').show();
                isValid = false;
            }
        });

        // Email validation
        $(form).find('input[type="email"]').each(function () {
            var email = $(this).val();
            var emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
            if (email && !emailRegex.test(email)) {
                $(this).addClass('is-invalid');
                $(this).siblings('.invalid-feedback').text('Please enter a valid email address').show();
                isValid = false;
            }
        });

        // Password confirmation validation (register form)
        if ($(form).attr('id') === 'registerForm') {
            var password = $('#password').val();
            var confirmPassword = $('#confirmPassword').val();
            if (password !== confirmPassword) {
                $('#confirmPassword').addClass('is-invalid');
                $('#confirmPassword').siblings('.invalid-feedback').text('Passwords do not match').show();
                isValid = false;
            }
        }

        // Number validation
        $(form).find('input[type="number"]').each(function () {
            var min = $(this).attr('min');
            var max = $(this).attr('max');
            var value = parseFloat($(this).val());

            if (min !== undefined && value < parseFloat(min)) {
                $(this).addClass('is-invalid');
                $(this).siblings('.invalid-feedback').text('Value must be at least ' + min).show();
                isValid = false;
            }

            if (max !== undefined && value > parseFloat(max)) {
                $(this).addClass('is-invalid');
                $(this).siblings('.invalid-feedback').text('Value must not exceed ' + max).show();
                isValid = false;
            }
        });

        // String length validation
        $(form).find('input[minlength], textarea[minlength]').each(function () {
            var minLength = $(this).attr('minlength');
            var value = $(this).val();
            if (value.length < parseInt(minLength)) {
                $(this).addClass('is-invalid');
                $(this).siblings('.invalid-feedback').text('Must be at least ' + minLength + ' characters').show();
                isValid = false;
            }
        });

        $(form).find('input[maxlength], textarea[maxlength]').each(function () {
            var maxLength = $(this).attr('maxlength');
            var value = $(this).val();
            if (value.length > parseInt(maxLength)) {
                $(this).addClass('is-invalid');
                $(this).siblings('.invalid-feedback').text('Must not exceed ' + maxLength + ' characters').show();
                isValid = false;
            }
        });

        if (!isValid) {
            e.preventDefault();
            // Scroll to first error
            var firstError = $(form).find('.is-invalid').first();
            if (firstError.length) {
                $('html, body').animate({
                    scrollTop: firstError.offset().top - 100
                }, 300);
            }
            return false;
        }

        return true;
    });

    // Real-time validation on input
    $('input, textarea, select').on('blur', function () {
        if ($(this).attr('required')) {
            if (!this.checkValidity()) {
                $(this).addClass('is-invalid');
                $(this).siblings('.invalid-feedback').show();
            } else {
                $(this).removeClass('is-invalid');
                $(this).siblings('.invalid-feedback').hide();
            }
        }
    });

    // Password match validation in real-time
    $('#confirmPassword').on('keyup', function () {
        var password = $('#password').val();
        var confirmPassword = $(this).val();
        if (confirmPassword && password !== confirmPassword) {
            $(this).addClass('is-invalid');
            $(this).siblings('.invalid-feedback').text('Passwords do not match').show();
        } else {
            $(this).removeClass('is-invalid');
            $(this).siblings('.invalid-feedback').hide();
        }
    });

    // Auto-dismiss alerts after 5 seconds
    setTimeout(function () {
        $('.alert').fadeOut('slow');
    }, 5000);

    // Confirmation dialogs
    $('form[onsubmit*="confirm"]').on('submit', function (e) {
        var message = $(this).attr('onsubmit').match(/confirm\('(.+)'\)/);
        if (message && message[1]) {
            if (!confirm(message[1])) {
                e.preventDefault();
                return false;
            }
        }
    });

    // Quantity input validation
    $('input[name="quantity"]').on('change', function () {
        var value = parseInt($(this).val());
        var min = parseInt($(this).attr('min')) || 1;
        var max = parseInt($(this).attr('max')) || 999;

        if (value < min) {
            $(this).val(min);
        } else if (value > max) {
            $(this).val(max);
        }
    });

    // Product search with debounce
    var searchTimeout;
    $('#productSearch').on('keyup', function () {
        clearTimeout(searchTimeout);
        var searchTerm = $(this).val();
        searchTimeout = setTimeout(function () {
            if (searchTerm.length >= 2) {
                // Trigger search
                $('#productSearchForm').submit();
            }
        }, 500);
    });
});
